package com.wootube.ioi.service.dto;

import lombok.Getter;

@Getter
public class EditUserRequestDto {
    private String name;

    public EditUserRequestDto(String name) {
        this.name = name;
    }
}
