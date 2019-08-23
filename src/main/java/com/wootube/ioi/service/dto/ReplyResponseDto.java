package com.wootube.ioi.service.dto;

import com.wootube.ioi.domain.model.User;
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
    private User writer;

    public static ReplyResponseDto of(Long id, String contents, LocalDateTime updateTime, User writer) {
        ReplyResponseDto replyResponseDto = new ReplyResponseDto();
        replyResponseDto.id = id;
        replyResponseDto.contents = contents;
        replyResponseDto.updateTime = updateTime;
        replyResponseDto.writer = writer;

        return replyResponseDto;
    }
}
