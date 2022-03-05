package com.hostfully.reservation.exception;

public class InvalidBookingDateRangeException extends ReservationException{

    public InvalidBookingDateRangeException() {
        super("The end date is before start date!");
    }
}
