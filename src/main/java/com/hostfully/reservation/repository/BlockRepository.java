package com.hostfully.reservation.repository;

import com.hostfully.reservation.model.Block;
import com.hostfully.reservation.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    List<Block> findAllByPropertyAndEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActive(
            Property property, LocalDate startDate, LocalDate endDate, boolean isActive);

}
