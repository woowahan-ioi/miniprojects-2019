package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import com.wootube.ioi.domain.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
