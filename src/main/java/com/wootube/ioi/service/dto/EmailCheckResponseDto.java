package com.wootube.ioi.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class EmailCheckResponseDto {
    private String message;

    public EmailCheckResponseDto() {
    }

    public EmailCheckResponseDto(String message) {
        this.message = message;
    }
}
