package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String contentPath;
    private LocalDateTime createTime;
}
