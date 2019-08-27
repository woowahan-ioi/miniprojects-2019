package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import com.wootube.ioi.domain.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    private User writer;

    public static CommentResponseDto of(Long id, String contents, LocalDateTime updateTime, User writer) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.id = id;
        commentResponseDto.contents = contents;
        commentResponseDto.updateTime = updateTime;
        commentResponseDto.writer = writer;

        return commentResponseDto;
    }
}
