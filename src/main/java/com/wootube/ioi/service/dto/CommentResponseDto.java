package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    private String writerName;
    private Long like;

    public static CommentResponseDto of(Long id, String contents, LocalDateTime updateTime, String writerName) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.id = id;
        commentResponseDto.contents = contents;
        commentResponseDto.updateTime = updateTime;
        commentResponseDto.writerName = writerName;
        commentResponseDto.like = 0L;

        return commentResponseDto;
    }

    public void setLike(Long like) {
        this.like = like;
    }
}
