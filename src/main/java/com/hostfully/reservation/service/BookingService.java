package com.hostfully.reservation.service;

import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.response.BookingResponse;

public interface BookingService {
    BookingResponse getBooking(Long bookingId);
    BookingResponse makeReservation(BookingRequest bookingRequest);
}
