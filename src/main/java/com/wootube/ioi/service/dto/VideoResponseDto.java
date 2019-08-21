package com.wootube.ioi.service.dto;

import com.wootube.ioi.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String contentPath;
    private LocalDateTime updateTime;
    private User writer;
}
