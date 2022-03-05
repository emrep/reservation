package com.hostfully.reservation.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(	name = "bookings")
public class Booking extends BaseModel {

    @ManyToOne
    @JoinColumn(name="property_id")
    private Property property;

    @Basic
    private LocalDate startDate;

    @Basic
    private LocalDate endDate;

    public Booking(Property property, LocalDate startDate, LocalDate endDate) {
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Booking() {

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