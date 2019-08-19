package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    //private String writerName;


    public static ReplyResponseDto of(Long id, String contents, LocalDateTime updateTime) {
        ReplyResponseDto replyResponseDto = new ReplyResponseDto();
        replyResponseDto.id = id;
        replyResponseDto.contents = contents;
        replyResponseDto.updateTime = updateTime;

        return replyResponseDto;
    }
}
