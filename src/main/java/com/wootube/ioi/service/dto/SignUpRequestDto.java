package com.wootube.ioi.service.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;

    public SignUpRequestDto(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
