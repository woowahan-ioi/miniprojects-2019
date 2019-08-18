package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyRequestDto {
    private String contents;

    public static ReplyRequestDto of(String contents) {
        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.contents = contents;
        return replyRequestDto;
    }
}
