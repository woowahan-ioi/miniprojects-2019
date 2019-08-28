package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchVideoException;
import com.wootube.ioi.domain.exception.NotMatchWriterException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_to_video"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;

    public static Comment of(String contents, Video video, User writer) {
        Comment comment = new Comment();
        comment.contents = contents;
        comment.video = video;
        comment.writer = writer;

        return comment;
    }

    public void update(User writer, Video video, String contents) {
        checkMatchWriter(writer);
        checkMatchVideo(video);
        this.contents = contents;
    }

    public void checkMatchWriter(User writer) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }
    }

    public void checkMatchVideo(Video video) {
        if (!this.video.equals(video)) {
            throw new NotMatchVideoException();
        }
    }
}
