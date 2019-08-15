package com.wootube.ioi.service.dto;

import lombok.Getter;

@Getter
public class EmailCheckRequestDto {
    private String email;

    public EmailCheckRequestDto() {

    }

    public EmailCheckRequestDto(String email) {
        this.email = email;
    }
}
