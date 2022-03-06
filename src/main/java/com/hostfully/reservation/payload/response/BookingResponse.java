package com.hostfully.reservation.payload.response;

import com.hostfully.reservation.model.Booking;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class BookingResponse {
    private final Long id;
    private final PropertyResponse property;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean isActive;
    private final LocalDateTime canceledAt;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.property = new PropertyResponse(booking.getProperty());
        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
        this.isActive = booking.isActive();
        this.canceledAt = booking.getCanceledAt();
    }
}
