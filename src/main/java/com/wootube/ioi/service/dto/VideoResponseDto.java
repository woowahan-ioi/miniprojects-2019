package com.wootube.ioi.service.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoResponseDto {
	private Long id;
	private String title;
	private String description;
	private String contentPath;
	private String writerProfileImage;
	private Long views;
	private LocalDateTime createTime;
	private String writerName;
	private Long writerId;
	private String thumbnailPath;
	private boolean isLike;

	public VideoResponseDto(Long id, String title, String description, String contentPath, String writerProfileImage, Long views, LocalDateTime createTime, String writerName, Long writerId, String thumbnailPath, boolean isLike) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.contentPath = contentPath;
		this.writerProfileImage = writerProfileImage;
		this.views = views;
		this.createTime = createTime;
		this.writerName = writerName;
		this.writerId = writerId;
		this.thumbnailPath = thumbnailPath;
		this.isLike = isLike;
	}
}
