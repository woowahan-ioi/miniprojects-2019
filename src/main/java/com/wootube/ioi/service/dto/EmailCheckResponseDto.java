package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCheckResponseDto {

    private static final String POSSIBLE_RESPONSE_MESSAGE = "possible";
    private static final String IMPOSSIBLE_RESPONSE_MESSAGE = "impossible";

    private String message;

    private EmailCheckResponseDto(String message) {
        this.message = message;
    }

    public static EmailCheckResponseDto possible() {
        return new EmailCheckResponseDto(POSSIBLE_RESPONSE_MESSAGE);
    }

    public static EmailCheckResponseDto impossible() {
        return new EmailCheckResponseDto(IMPOSSIBLE_RESPONSE_MESSAGE);
    }
}
