package com.reservation.service;

import com.reservation.payload.request.BlockRequest;
import com.reservation.payload.response.BlockResponse;

public interface BlockService {
    BlockResponse createBlock(BlockRequest blockRequest);
    BlockResponse deleteBlock(Long blockId);
}
