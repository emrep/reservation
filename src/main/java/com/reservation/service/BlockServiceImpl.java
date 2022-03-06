package com.reservation.service;

import com.reservation.model.Block;
import com.reservation.payload.request.BlockRequest;
import com.reservation.payload.response.BlockResponse;
import com.reservation.repository.BlockRepository;
import com.reservation.repository.PropertyRepository;
import com.reservation.model.Property;
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
