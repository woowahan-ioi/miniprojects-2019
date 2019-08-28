package com.wootube.ioi.domain.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class VideoLike extends BaseEntity {
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_video_like_to_video"), nullable = false)
	private Video video;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_video_like_to_user"), nullable = false)
	private User likeUser;

	public VideoLike(User user, Video video) {
		this.video = video;
		this.likeUser = user;
	}
}
