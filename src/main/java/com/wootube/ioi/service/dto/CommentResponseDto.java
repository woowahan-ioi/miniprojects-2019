package com.wootube.ioi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    //private String authorName;
}
