package com.wootube.ioi.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyLikeResponseDto {
    private long count;

    public ReplyLikeResponseDto(long count) {
        this.count = count;
    }
}
