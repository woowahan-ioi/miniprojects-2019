package com.wootube.ioi.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoRequestDto {
    private String title;
    private String description;
    private String contentPath;
}
