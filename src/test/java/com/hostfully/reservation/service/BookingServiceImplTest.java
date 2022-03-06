package com.hostfully.reservation.service;

import com.hostfully.reservation.exception.BlockException;
import com.hostfully.reservation.exception.InvalidBookingDateRangeException;
import com.hostfully.reservation.exception.OverlappingBookingException;
import com.hostfully.reservation.model.*;
import com.hostfully.reservation.payload.request.BookingRequest;
import com.hostfully.reservation.payload.request.BookingUpdateRequest;
import com.hostfully.reservation.payload.response.BookingResponse;
import com.hostfully.reservation.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookingServiceImplTest {

    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    PropertyRepository propertyRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    BlockRepository blockRepository;
    @MockBean
    BookingOperationRepository bookingOperationRepository;
    @Autowired
    BookingService bookingService;

    @Test
    void getBooking() {
        Property property = getProperty();
        User guest = getUser();
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        Booking booking = new Booking(property, guest, startDate, endDate);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        BookingResponse response = bookingService.getBooking(1L);
        assertEquals(response.getStartDate(), startDate);
        assertEquals(response.getEndDate(), endDate);
    }

    @Test
    void createBooking() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        BookingRequest bookingRequest = getBookingRequest(startDate, endDate);
        Property property = getProperty();
        User guest = getUser();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(bookingRepository.findOverlappingBookings(bookingRequest.getPropertyId(), bookingRequest.getStartDate(), bookingRequest.getEndDate())).thenReturn(Collections.emptyList());
        when(blockRepository.findAllByPropertyAndEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActive(property, bookingRequest.getStartDate(), bookingRequest.getEndDate(), true)).thenReturn(Collections.emptyList());
        when(userRepository.findById(1L)).thenReturn(Optional.of(guest));
        Booking booking = new Booking(property, guest, startDate, endDate);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingOperationRepository.save(any())).thenReturn(new BookingOperation());
        BookingResponse response = bookingService.createBooking(bookingRequest);
        assertEquals(response.getStartDate(), startDate);
    }

    @Test
    void testCheckingBookingDateRange() {
        LocalDate startDate = LocalDate.of(2022, 4, 27);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        BookingRequest bookingRequest = getBookingRequest(startDate, endDate);
        try {
            bookingService.createBooking(bookingRequest);
            fail("Expected InvalidBookingDateRangeException was not occured.");
        } catch (InvalidBookingDateRangeException ex) {
            assertTrue(true);
        }

    }

    @Test
    void testCheckingOverlappingBookings() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        BookingRequest bookingRequest = getBookingRequest(startDate, endDate);
        Property property = getProperty();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(bookingRepository.findOverlappingBookings(bookingRequest.getPropertyId(), bookingRequest.getStartDate(), bookingRequest.getEndDate())).thenReturn(List.of(1L));

        try {
            bookingService.createBooking(bookingRequest);
            fail("Expected OverlappingBookingException was not occured.");
        } catch (OverlappingBookingException ex) {
            assertTrue(true);
        }

    }

    @Test
    void testCheckingBlockedDates() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        BookingRequest bookingRequest = getBookingRequest(startDate, endDate);
        Property property = getProperty();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(bookingRepository.findOverlappingBookings(bookingRequest.getPropertyId(), bookingRequest.getStartDate(), bookingRequest.getEndDate())).thenReturn(Collections.emptyList());
        Block block = new Block();
        block.setStartDate(startDate);
        block.setEndDate(endDate);
        when(blockRepository.findAllByPropertyAndEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActive(property, bookingRequest.getStartDate(), bookingRequest.getEndDate(), true)).thenReturn(List.of(block));

        try {
            bookingService.createBooking(bookingRequest);
            fail("Expected BlockException was not occured.");
        } catch (BlockException ex) {
            assertTrue(true);
        }

    }

    @Test
    void updateBooking() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate updatedStartDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        LocalDate updatedEndDate = LocalDate.of(2022, 4, 23);
        BookingUpdateRequest bookingUpdateRequest = getBookingUpdateRequest(updatedStartDate, updatedEndDate);
        Property property = getProperty();
        User guest = getUser();

        Long bookingId = 1L;
        Booking booking = getBooking(property, guest, startDate, endDate, bookingId);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(property.getId())).thenReturn(Optional.of(property));
        when(bookingRepository.findOverlappingBookingsForUpdate(property.getId(), booking.getId(), bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate())).thenReturn(Collections.emptyList());
        when(blockRepository.findAllByPropertyAndEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActive(property, bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate(), true)).thenReturn(Collections.emptyList());

        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingOperationRepository.save(any())).thenReturn(new BookingOperation());
        BookingResponse response = bookingService.updateBooking(bookingId, bookingUpdateRequest);
        assertEquals(response.getStartDate(), updatedStartDate);
        assertEquals(response.getEndDate(), updatedEndDate);
    }

    @Test
    void testCheckingOverlappingBookingsForUpdate() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate updatedStartDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        LocalDate updatedEndDate = LocalDate.of(2022, 4, 23);
        BookingUpdateRequest bookingUpdateRequest = getBookingUpdateRequest(updatedStartDate, updatedEndDate);
        Property property = getProperty();
        User guest = getUser();

        Long bookingId = 1L;
        Booking booking = getBooking(property, guest, startDate, endDate, bookingId);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(bookingRepository.findOverlappingBookingsForUpdate(property.getId(), booking.getId(), bookingUpdateRequest.getStartDate(), bookingUpdateRequest.getEndDate())).thenReturn(List.of(1L));

        try {
            bookingService.updateBooking(bookingId, bookingUpdateRequest);
            fail("Expected OverlappingBookingException was not occured.");
        } catch (OverlappingBookingException ex) {
            assertTrue(true);
        }
    }

    @Test
    void cancelBooking() {
        Property property = getProperty();
        User guest = getUser();
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        Long bookingId = 1L;
        Booking booking = getBooking(property, guest, startDate, endDate, bookingId);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingOperationRepository.save(any())).thenReturn(new BookingOperation());
        BookingResponse response = bookingService.cancelBooking(bookingId);
        assertFalse(response.isActive());
    }

    private User getUser() {
        User guest = new User();
        guest.setId(1L);
        return guest;
    }

    private Property getProperty() {
        User owner = new User();
        Property property = new Property();
        property.setId(1L);
        property.setOwner(owner);
        return property;
    }

    private BookingRequest getBookingRequest(LocalDate startDate, LocalDate endDate) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setPropertyId(1L);
        bookingRequest.setGuestId(1L);
        bookingRequest.setStartDate(startDate);
        bookingRequest.setEndDate(endDate);
        return bookingRequest;
    }

    private BookingUpdateRequest getBookingUpdateRequest(LocalDate startDate, LocalDate endDate) {
        BookingUpdateRequest bookingUpdateRequest = new BookingUpdateRequest();
        bookingUpdateRequest.setStartDate(startDate);
        bookingUpdateRequest.setEndDate(endDate);
        return bookingUpdateRequest;
    }

    private Booking getBooking(Property property, User guest, LocalDate startDate, LocalDate endDate, Long bookingId) {
        Booking booking = new Booking(property, guest, startDate, endDate);
        booking.setId(bookingId);
        return booking;
    }

}