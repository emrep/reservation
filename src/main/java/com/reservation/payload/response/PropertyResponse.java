package com.reservation.payload.response;

import com.reservation.model.Property;
import lombok.Getter;

@Getter
public class PropertyResponse {
    private final Long id;
    private final String name;
    private final UserResponse owner;

    public PropertyResponse(Property property) {
        this.id = property.getId();
        this.name = property.getName();
        this.owner = new UserResponse(property.getOwner());
    }
}
