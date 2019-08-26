package com.wootube.ioi.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicUpdate
public class Video extends BaseEntity {
    @Column(nullable = false,
            length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false)
    private String contentPath;

    @Lob
    @Column(nullable = false)
    private String originFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_video_to_user"), nullable = false)
    private User writer;

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(Video updateVideo) {
        if (updateVideo.contentPath != null) {
            this.contentPath = updateVideo.contentPath;
        }
        this.title = updateVideo.title;
        this.description = updateVideo.description;
    }

    public void updateContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public void initialize(String contentPath, String originFileName, User writer) {
        this.contentPath = contentPath;
        this.originFileName = originFileName;
        this.writer = writer;
    }

    public boolean matchWriter(Long userId) {
        return writer.isSameEntity(userId);
    }
}
