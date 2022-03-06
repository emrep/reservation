package com.hostfully.reservation.service;

import com.hostfully.reservation.exception.InvalidBookingDateRangeException;
import com.hostfully.reservation.exception.OverlappingBookingException;
import com.hostfully.reservation.model.Booking;
import com.hostfully.reservation.model.BookingOperation;
import com.hostfully.reservation.model.EnumOperationType;
import com.hostfully.reservation.model.Property;
import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.request.BookingUpdateRequest;
import com.hostfully.reservation.payload.response.BookingResponse;
import com.hostfully.reservation.repository.BookingOperationRepository;
import com.hostfully.reservation.repository.BookingRepository;
import com.hostfully.reservation.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    final BookingRepository bookingRepository;
    final PropertyRepository propertyRepository;
    final BookingOperationRepository bookingOperationRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PropertyRepository propertyRepository,
                              BookingOperationRepository bookingOperationRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.bookingOperationRepository = bookingOperationRepository;
    }

    @Override
    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return new BookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        checkBookingDateRange(bookingRequest.getStartDate(), bookingRequest.getEndDate());
        Optional<Property> property = propertyRepository.findById(bookingRequest.getPropertyId()); // lock the corresponding property row in the table
        checkOverlappingBookings(bookingRequest);
        Booking booking = bookingRepository.save(
                new Booking(property.orElseThrow(), bookingRequest.getStartDate(), bookingRequest.getEndDate()));
        createBookingOperation(booking, EnumOperationType.CREATION);
        return new BookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long bookingId, BookingUpdateRequest bookingUpdateRequest) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        checkBookingDateRange(bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate());
        propertyRepository.findById(booking.getProperty().getId()); // lock the corresponding property row in the table
        checkOverlappingBookingsForUpdate(booking, bookingUpdateRequest);
        booking.setStartDate(bookingUpdateRequest.getStartDate());
        booking.setEndDate(bookingUpdateRequest.getEndDate());
        booking = bookingRepository.save(booking);
        createBookingOperation(booking, EnumOperationType.UPDATE);
        return new BookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setActive(false);
        booking = bookingRepository.save(booking);
        createBookingOperation(booking, EnumOperationType.DELETION);
        return new BookingResponse(booking);
    }

    private void checkBookingDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidBookingDateRangeException();
        }
    }

    private void checkOverlappingBookings(BookingRequest bookingRequest) {
        List<Long> overlappingBookings = bookingRepository.findOverlappingBookings(
                bookingRequest.getPropertyId(), bookingRequest.getStartDate(), bookingRequest.getEndDate());
        if (!overlappingBookings.isEmpty()) {
            throw new OverlappingBookingException();
        }
    }

    private void checkOverlappingBookingsForUpdate(Booking booking, BookingUpdateRequest bookingUpdateRequest) {
        List<Long> overlappingBookings = bookingRepository.findOverlappingBookingsForUpdate(
                booking.getProperty().getId(), booking.getId(), bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate());
        if (!overlappingBookings.isEmpty()) {
            throw new OverlappingBookingException();
        }
    }

    private void createBookingOperation(Booking booking, EnumOperationType type) {
        BookingOperation bookingOperation = new BookingOperation(booking, type);
        bookingOperationRepository.save(bookingOperation);
    }

}
