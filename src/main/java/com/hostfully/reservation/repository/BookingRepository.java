package com.hostfully.reservation.repository;

import com.hostfully.reservation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT id FROM Booking WHERE PROPERTY_ID = :propertyId AND :startDate <= END_DATE AND :endDate >= START_DATE AND IS_ACTIVE=true")
    List<Long> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT id FROM Booking WHERE id <> :bookingId AND PROPERTY_ID = :propertyId AND :startDate <= END_DATE AND :endDate >= START_DATE AND IS_ACTIVE=true")
    List<Long> findOverlappingBookingsForUpdate(@Param("propertyId") Long propertyId,
                                                @Param("bookingId") Long bookingId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
}
