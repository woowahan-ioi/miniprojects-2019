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
    private String writerProfileImageUrl;
    private Long like;
    private boolean likedUser;

    public static ReplyResponseDto of(Long id, String contents, LocalDateTime updateTime, String writerName, String writerProfileImageUrl) {
        ReplyResponseDto replyResponseDto = new ReplyResponseDto();
        replyResponseDto.id = id;
        replyResponseDto.contents = contents;
        replyResponseDto.updateTime = updateTime;
        replyResponseDto.writerName = writerName;
        replyResponseDto.writerProfileImageUrl = writerProfileImageUrl;
        replyResponseDto.like = 0L;
        replyResponseDto.likedUser = false;

        return replyResponseDto;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public void setLikedUser(boolean likedUser) {
        this.likedUser = likedUser;
    }
}
