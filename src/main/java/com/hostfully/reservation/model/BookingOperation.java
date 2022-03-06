package com.hostfully.reservation.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(	name = "booking_operations")
public class BookingOperation extends BaseModel {

    @ManyToOne
    @JoinColumn(name="booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumOperationType type;

    public BookingOperation() {
    }

    public BookingOperation(Booking booking, EnumOperationType type) {
        this.booking = booking;
        this.type = type;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public EnumOperationType getType() {
        return type;
    }

    public void setType(EnumOperationType type) {
        this.type = type;
    }
}
