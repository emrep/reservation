package com.hostfully.reservation.controller;

import com.hostfully.reservation.payload.request.BlockRequest;
import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.request.BookingUpdateRequest;
import com.hostfully.reservation.service.BlockService;
import com.hostfully.reservation.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReservationController {

    final BookingService bookingService;
    final BlockService blockService;

    public ReservationController(BookingService bookingService, BlockService blockService) {
        this.bookingService = bookingService;
        this.blockService = blockService;
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest));
    }

    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable Long bookingId, @Valid @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, bookingUpdateRequest));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @PostMapping("/blocks")
    public ResponseEntity<?> createBlock(@Valid @RequestBody BlockRequest blockRequest) {
        return ResponseEntity.ok(blockService.createBlock(blockRequest));
    }

    @DeleteMapping("/blocks/{blockId}")
    public ResponseEntity<?> deleteBlock(@PathVariable Long blockId) {
        return ResponseEntity.ok(blockService.deleteBlock(blockId));
    }

}
