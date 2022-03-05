package com.hostfully.reservation.exception;

public class ItemNotFoundException extends ReservationException{

    public ItemNotFoundException() {
        super("The entity could not be found!");
    }
}
