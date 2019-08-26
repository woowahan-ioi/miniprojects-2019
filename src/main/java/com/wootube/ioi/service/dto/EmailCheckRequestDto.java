package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCheckRequestDto {
    private String email;

    public EmailCheckRequestDto(String email) {
        this.email = email;
    }
}
