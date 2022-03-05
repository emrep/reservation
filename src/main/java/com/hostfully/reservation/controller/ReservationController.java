package com.hostfully.reservation.controller;

import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ReservationController {

    final BookingService bookingService;

    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> addFaculty(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.makeReservation(bookingRequest));
    }
}
