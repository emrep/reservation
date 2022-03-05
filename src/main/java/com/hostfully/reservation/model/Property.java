package com.hostfully.reservation.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "properties", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Property extends BaseModel {

    @NotBlank
    @Size(max = 150)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
