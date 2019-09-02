package com.wootube.ioi.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoLikeResponseDto {
    private long count;

    public VideoLikeResponseDto(long count) {
        this.count = count;
    }
}
