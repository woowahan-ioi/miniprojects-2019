package com.wootube.ioi.service.dto;

import lombok.Getter;

@Getter
public class EmailCheckResponseDto {
    private String message;

    public EmailCheckResponseDto() {
    }

    public EmailCheckResponseDto(String message) {
        this.message = message;
    }
}
