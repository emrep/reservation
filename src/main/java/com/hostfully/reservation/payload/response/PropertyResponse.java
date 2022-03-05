package com.hostfully.reservation.payload.response;

import com.hostfully.reservation.model.Property;
import lombok.Getter;

@Getter
public class PropertyResponse {
    private final Long id;
    private final String name;

    public PropertyResponse(Property property) {
        this.id = property.getId();
        this.name = property.getName();
    }
}
