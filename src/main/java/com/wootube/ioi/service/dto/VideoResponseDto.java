package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String contentPath;
    private Long views;
    private LocalDateTime createTime;
    private String writerName;
    private Long writerId;
    private String thumbnailPath;

    public VideoResponseDto(Long id, String title, String description, String contentPath, Long views, LocalDateTime createTime, String writerName, Long writerId, String thumbnailPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentPath = contentPath;
        this.views = views;
        this.createTime = createTime;
        this.writerName = writerName;
        this.writerId = writerId;
        this.thumbnailPath = thumbnailPath;
    }
}
