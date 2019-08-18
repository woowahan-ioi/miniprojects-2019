package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyResponseDto {
    private Long Id;
    private String contents;
    private LocalDateTime updateTime;
    //private String writerName;
}
