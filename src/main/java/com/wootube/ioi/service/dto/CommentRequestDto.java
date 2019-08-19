package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {
    private String contents;

    public static CommentRequestDto of(String contents) {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.contents = contents;
        return commentRequestDto;
    }
}
