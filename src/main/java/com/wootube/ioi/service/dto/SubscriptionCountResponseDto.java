package com.wootube.ioi.service.dto;

import lombok.Getter;

@Getter
public class SubscriptionCountResponseDto {

    private final long count;

    public SubscriptionCountResponseDto(long count) {
        this.count = count;
    }
}
