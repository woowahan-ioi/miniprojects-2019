package com.wootube.ioi.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Video extends BaseEntity {
    private static final int CONTENT_PATH = 0;
    private static final int THUMBNAIL_PATH = 1;

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
    private String thumbnailPath;

    @Lob
    @Column(nullable = false)
    private String originFileName;

    @Lob
    @Column(nullable = false)
    private String thumbnailFileName;

    @Column(columnDefinition = "bigint default 0")
    private long views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_video_to_user"), nullable = false)
    private User writer;

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(Video updateVideo) {
        this.title = updateVideo.title;
        this.description = updateVideo.description;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateVideo(String contentPath, String originFileName, String thumbnailPath, String thumbnailFileName) {
        this.contentPath = contentPath;
        this.originFileName = originFileName;
        this.thumbnailPath = thumbnailPath;
        this.thumbnailFileName = thumbnailFileName;
    }

    public void initialize(String contentPath, String thumbnailPath, String originFileName, String thumbnailFileName, User writer) {
        this.contentPath = contentPath;
        this.thumbnailPath = thumbnailPath;
        this.originFileName = originFileName;
        this.thumbnailFileName = thumbnailFileName;
        this.writer = writer;
    }

    public boolean matchWriter(Long userId) {
        return writer.isSameEntity(userId);
    }

    public void increaseViews() {
        this.views++;
    }

}
