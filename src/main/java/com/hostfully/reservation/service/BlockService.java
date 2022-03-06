package com.hostfully.reservation.service;

import com.hostfully.reservation.payload.request.BlockRequest;
import com.hostfully.reservation.payload.response.BlockResponse;

public interface BlockService {
    BlockResponse createBlock(BlockRequest blockRequest);
    BlockResponse deleteBlock(Long blockId);
}
