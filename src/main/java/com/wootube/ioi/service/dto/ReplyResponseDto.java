package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    private String writerName;

    public static ReplyResponseDto of(Long id, String contents, LocalDateTime updateTime, String writerName) {
        ReplyResponseDto replyResponseDto = new ReplyResponseDto();
        replyResponseDto.id = id;
        replyResponseDto.contents = contents;
        replyResponseDto.updateTime = updateTime;
        replyResponseDto.writerName = writerName;

        return replyResponseDto;
    }
}
