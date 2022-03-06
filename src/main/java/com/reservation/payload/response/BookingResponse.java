package com.reservation.payload.response;

import com.reservation.model.Booking;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class BookingResponse {
    private final Long id;
    private final PropertyResponse property;
    private final UserResponse guest;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean isActive;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.property = new PropertyResponse(booking.getProperty());
        this.guest = new UserResponse(booking.getGuest());
        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
        this.isActive = booking.isActive();
    }
}
