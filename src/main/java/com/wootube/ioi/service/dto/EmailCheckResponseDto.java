package com.wootube.ioi.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class EmailCheckResponseDto {

    private static final String POSSIBLE_RESPONSE_MESSAGE = "possible";
    private static final String IMPOSSIBLE_RESPONSE_MESSAGE = "impossible";

    private String message;

    private EmailCheckResponseDto() {
    }

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
