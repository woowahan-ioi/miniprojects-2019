package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    private String writerName;

    public static CommentResponseDto of(Long id, String contents, LocalDateTime updateTime, String writerName) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.id = id;
        commentResponseDto.contents = contents;
        commentResponseDto.updateTime = updateTime;
        commentResponseDto.writerName = writerName;

        return commentResponseDto;
    }
}
