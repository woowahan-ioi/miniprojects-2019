package com.wootube.ioi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReplyResponseDto {
    private Long Id;
    private String contents;
    private LocalDateTime updateTime;
    //private String writerName;
}
