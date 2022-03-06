package com.hostfully.reservation.service;

import com.hostfully.reservation.model.Block;
import com.hostfully.reservation.model.Property;
import com.hostfully.reservation.model.User;
import com.hostfully.reservation.payload.request.BlockRequest;
import com.hostfully.reservation.payload.response.BlockResponse;
import com.hostfully.reservation.repository.BlockRepository;
import com.hostfully.reservation.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlockServiceImplTest {

    @MockBean
    BlockRepository blockRepository;
    @MockBean
    PropertyRepository propertyRepository;
    @Autowired
    BlockService blockService;

    @Test
    void createBlock() {
        LocalDate startDate = LocalDate.of(2022, 4, 20);
        LocalDate endDate = LocalDate.of(2022, 4, 23);
        BlockRequest blockRequest = new BlockRequest();
        blockRequest.setPropertyId(1L);
        blockRequest.setStartDate(startDate);
        blockRequest.setEndDate(endDate);
        User owner = new User();
        Property property = new Property();
        property.setId(1L);
        property.setOwner(owner);
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        Block block = new Block(property, blockRequest.getStartDate(), blockRequest.getEndDate());
        when(blockRepository.save(any())).thenReturn(block);
        BlockResponse blockResponse = blockService.createBlock(blockRequest);
        assertEquals(blockResponse.getStartDate(), startDate);
        assertEquals(blockResponse.getEndDate(), endDate);
    }

    @Test
    void deleteBlock() {
        User owner = new User();
        Property property = new Property();
        property.setId(1L);
        property.setOwner(owner);
        Block block = new Block();
        block.setProperty(property);
        when(blockRepository.findById(1L)).thenReturn(Optional.of(block));
        when(blockRepository.save(any())).thenReturn(block);
        BlockResponse blockResponse = blockService.deleteBlock(1L);
        assertFalse(blockResponse.isActive());
    }
}