package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionCheckResponseDto {
    private boolean isSubscribe;

    public SubscriptionCheckResponseDto(boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
}
