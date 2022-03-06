package com.reservation.exception;

public class OverlappingBookingException extends ReservationException{

    public OverlappingBookingException() {
        super("The property has been booked by another guest!");
    }
}
