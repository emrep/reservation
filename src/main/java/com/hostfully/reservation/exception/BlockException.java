package com.hostfully.reservation.exception;

import com.hostfully.reservation.model.Block;

public class BlockException extends ReservationException{

    public BlockException(Block block) {
        super("The property has been blocked by the property owner between " + block.getStartDate() + " and " + block.getEndDate());
    }
}
