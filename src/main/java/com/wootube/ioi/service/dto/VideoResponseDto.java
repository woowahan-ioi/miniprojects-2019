package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import com.wootube.ioi.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String contentPath;
    private long views;
    private LocalDateTime createTime;
    private String writerName;
    private Long writerId;

    public VideoResponseDto(Long id, String title, String description, String contentPath, long views, LocalDateTime createTime, String writerName, Long writerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentPath = contentPath;
        this.views = views;
        this.createTime = createTime;
        this.writerName = writerName;
        this.writerId = writerId;
    }
}
