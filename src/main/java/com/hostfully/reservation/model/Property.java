package com.hostfully.reservation.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "properties", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Property extends BaseModel {

    @NotBlank
    @Size(max = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
