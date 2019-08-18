package com.wootube.ioi.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

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

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }
}
