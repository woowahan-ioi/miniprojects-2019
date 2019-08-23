package com.wootube.ioi.service.dto;

import com.wootube.ioi.domain.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String contentPath;
    private LocalDateTime updateTime;
    private User writer;
}
