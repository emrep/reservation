package com.hostfully.reservation.payload.response;

import com.hostfully.reservation.model.Booking;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookingResponse {
    private final Long id;
    private final PropertyResponse property;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.property = new PropertyResponse(booking.getProperty());
        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
    }
}
