package com.wootube.ioi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String contents;
    private String authorName = "hyo";
    private LocalDateTime updateTime;

    public CommentResponse(Long id, String contents, LocalDateTime updateTime) {
        this.id = id;
        this.contents = contents;
        this.updateTime = updateTime;
    }
}
