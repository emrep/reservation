package com.hostfully.reservation.service;

import com.hostfully.reservation.model.Block;
import com.hostfully.reservation.model.Property;
import com.hostfully.reservation.payload.request.BlockRequest;
import com.hostfully.reservation.payload.response.BlockResponse;
import com.hostfully.reservation.repository.BlockRepository;
import com.hostfully.reservation.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockServiceImpl implements BlockService{

    final BlockRepository blockRepository;
    final PropertyRepository propertyRepository;

    public BlockServiceImpl(BlockRepository blockRepository, PropertyRepository propertyRepository) {
        this.blockRepository = blockRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    @Transactional
    public BlockResponse createBlock(BlockRequest blockRequest) {
        Property property = propertyRepository.findById(blockRequest.getPropertyId()).orElseThrow();
        Block block = new Block(property, blockRequest.getStartDate(), blockRequest.getEndDate());
        block = blockRepository.save(block);
        return new BlockResponse(block);
    }

    @Override
    @Transactional
    public BlockResponse deleteBlock(Long blockId) {
        Block block = blockRepository.findById(blockId).orElseThrow();
        block.setActive(false);
        return new BlockResponse(block);
    }
}
