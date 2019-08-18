package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    //private String authorName;
}
