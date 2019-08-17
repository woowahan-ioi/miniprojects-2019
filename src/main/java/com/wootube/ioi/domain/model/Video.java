package com.wootube.ioi.domain.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
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

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public void update(Video updateVideo) {
        if (updateVideo.contentPath != null) {
            this.contentPath = updateVideo.contentPath;
        }
        this.title = updateVideo.title;
        this.description = updateVideo.description;
    }
}
