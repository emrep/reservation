package com.hostfully.reservation.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull
    private Long propertyId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}

