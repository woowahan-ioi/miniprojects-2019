package com.wootube.ioi.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentLikeResponseDto {
    private long count;

    public CommentLikeResponseDto(long count) {
        this.count = count;
    }
}
