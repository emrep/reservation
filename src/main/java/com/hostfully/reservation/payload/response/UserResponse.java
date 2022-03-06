package com.hostfully.reservation.payload.response;

import com.hostfully.reservation.model.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;

    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
