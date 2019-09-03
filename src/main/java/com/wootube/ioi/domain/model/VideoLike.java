package com.wootube.ioi.domain.model;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
public class VideoLike extends BaseEntity {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_video_like_to_video"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_video_like_to_user"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User likeUser;

    public VideoLike(User user, Video video) {
        this.video = video;
        this.likeUser = user;
    }
}
