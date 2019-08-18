package com.wootube.ioi.domain.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicUpdate
@EqualsAndHashCode(of = "id")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column
    @UpdateTimestamp
    private LocalDateTime updateTime;

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
