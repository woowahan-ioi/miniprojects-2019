package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoRequestDto {
    private String title;
    private String description;

    public VideoRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
