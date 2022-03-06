package com.reservation.repository;

import com.reservation.model.BookingOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingOperationRepository extends JpaRepository<BookingOperation, Long> {
}
