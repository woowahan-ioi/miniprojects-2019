package com.wootube.ioi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime updateTime;
    private String writerName;
    private String writerProfileImageUrl;
    private Long like;
    private boolean likedUser = false;

    public static CommentResponseDto of(Long id, String contents, LocalDateTime updateTime, String writerName, String writerProfileImageUrl) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.id = id;
        commentResponseDto.contents = contents;
        commentResponseDto.updateTime = updateTime;
        commentResponseDto.writerName = writerName;
        commentResponseDto.writerProfileImageUrl = writerProfileImageUrl;
        commentResponseDto.like = 0L;
        return commentResponseDto;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public void setLikedUser(boolean likedUser) {
        this.likedUser = likedUser;
    }
}
