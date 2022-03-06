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

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> book(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.book(bookingRequest));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}
