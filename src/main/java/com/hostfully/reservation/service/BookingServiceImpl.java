package com.hostfully.reservation.service;

import com.hostfully.reservation.exception.InvalidBookingDateRangeException;
import com.hostfully.reservation.exception.OverlappingBookingException;
import com.hostfully.reservation.model.Booking;
import com.hostfully.reservation.model.Property;
import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.response.BookingResponse;
import com.hostfully.reservation.repository.BookingRepository;
import com.hostfully.reservation.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    final BookingRepository bookingRepository;
    final PropertyRepository propertyRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public BookingResponse getBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return new BookingResponse(booking.orElseThrow());
    }

    @Override
    @Transactional
    public BookingResponse makeReservation(BookingRequest bookingRequest) {
        if (bookingRequest.getStartDate().isAfter(bookingRequest.getEndDate())) {
            throw new InvalidBookingDateRangeException();
        }
        Optional<Property> property = propertyRepository.findById(bookingRequest.getPropertyId());
        List<Long> overlappingBookings = bookingRepository.findOverlappingBookings(bookingRequest.getPropertyId(), bookingRequest.getStartDate(), bookingRequest.getEndDate());
        if (overlappingBookings.isEmpty()) {
            Booking booking = bookingRepository.save(new Booking(property.orElseThrow(), bookingRequest.getStartDate(), bookingRequest.getEndDate()));
            return new BookingResponse(booking);
        } else {
            throw new OverlappingBookingException();
        }
    }
}
