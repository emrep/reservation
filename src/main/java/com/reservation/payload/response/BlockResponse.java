package com.reservation.payload.response;

import com.reservation.model.Block;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BlockResponse {
    private final Long id;
    private final PropertyResponse property;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean isActive;

    public BlockResponse(Block block) {
        this.id = block.getId();
        this.property = new PropertyResponse(block.getProperty());
        this.startDate = block.getStartDate();
        this.endDate = block.getEndDate();
        this.isActive = block.isActive();
    }
}
