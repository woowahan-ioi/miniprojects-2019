package com.wootube.ioi.web.dto;

import lombok.Getter;

@Getter
public class LogInRequestDto {
    private String email;
    private String password;

    public LogInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
