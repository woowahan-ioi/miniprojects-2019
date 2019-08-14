package com.wootube.ioi.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
	private Long id;
	private String title;
	private String description;
	private String contentPath;
	private LocalDateTime createTime;
}
