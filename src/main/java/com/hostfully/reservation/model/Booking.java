package com.hostfully.reservation.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(	name = "bookings")
public class Booking extends BaseModel {

    @ManyToOne
    @JoinColumn(name="property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name="guest_id")
    private User guest;

    @Basic
    private LocalDate startDate;

    @Basic
    private LocalDate endDate;

    public Booking(Property property, User guest, LocalDate startDate, LocalDate endDate) {
        this.property = property;
        this.guest = guest;
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

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
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
