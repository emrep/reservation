package com.hostfully.reservation.service;

import com.hostfully.reservation.exception.BlockException;
import com.hostfully.reservation.exception.InvalidBookingDateRangeException;
import com.hostfully.reservation.exception.OverlappingBookingException;
import com.hostfully.reservation.model.*;
import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.request.BookingUpdateRequest;
import com.hostfully.reservation.payload.response.BookingResponse;
import com.hostfully.reservation.repository.BlockRepository;
import com.hostfully.reservation.repository.BookingOperationRepository;
import com.hostfully.reservation.repository.BookingRepository;
import com.hostfully.reservation.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    final BookingRepository bookingRepository;
    final PropertyRepository propertyRepository;
    final BlockRepository blockRepository;
    final BookingOperationRepository bookingOperationRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PropertyRepository propertyRepository,
                              BlockRepository blockRepository, BookingOperationRepository bookingOperationRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.blockRepository = blockRepository;
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
        Property property = propertyRepository.findById(bookingRequest.getPropertyId()).orElseThrow(); // lock the corresponding property row in the table
        checkOverlappingBookings(bookingRequest);
        checkBlockedDates(property, bookingRequest.getStartDate(), bookingRequest.getEndDate());
        Booking booking = bookingRepository.save(
                new Booking(property, bookingRequest.getStartDate(), bookingRequest.getEndDate()));
        createBookingOperation(booking, EnumOperationType.CREATION);
        return new BookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long bookingId, BookingUpdateRequest bookingUpdateRequest) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        checkBookingDateRange(bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate());
        Property property = propertyRepository.findById(booking.getProperty().getId()).orElseThrow(); // lock the corresponding property row in the table
        checkOverlappingBookingsForUpdate(booking, bookingUpdateRequest);
        checkBlockedDates(property, bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate());
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

    private void checkBlockedDates(Property property, LocalDate startDate, LocalDate endDate) {
        List<Block> blocks = blockRepository.findAllByPropertyAndEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActive(
                property, startDate, endDate, true);
        if (!blocks.isEmpty()) {
            throw new BlockException(blocks.get(0));
        }
    }

    private void createBookingOperation(Booking booking, EnumOperationType type) {
        BookingOperation bookingOperation = new BookingOperation(booking, type);
        bookingOperationRepository.save(bookingOperation);
    }

}
