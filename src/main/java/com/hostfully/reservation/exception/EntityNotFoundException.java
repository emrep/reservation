package com.hostfully.reservation.exception;

public class EntityNotFoundException extends ReservationException{

    public EntityNotFoundException() {
        super("The entity could not be found");
    }
}
