package com.reservation.service;

import com.reservation.payload.request.BookingRequest;
import com.reservation.payload.request.BookingUpdateRequest;
import com.reservation.payload.response.BookingResponse;

public interface BookingService {
    BookingResponse getBooking(Long bookingId);
    BookingResponse createBooking(BookingRequest bookingRequest);
    BookingResponse updateBooking(Long bookingId, BookingUpdateRequest bookingUpdateRequest);
    BookingResponse cancelBooking(Long bookingId);
}
