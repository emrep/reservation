package com.hostfully.reservation.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(	name = "blocks")
public class Block extends BaseModel {

    @ManyToOne
    @JoinColumn(name="property_id")
    private Property property;

    @Basic
    private LocalDate startDate;

    @Basic
    private LocalDate endDate;

    public Block(Property property, LocalDate startDate, LocalDate endDate) {
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Block() {

    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
