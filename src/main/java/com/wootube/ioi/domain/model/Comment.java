package com.wootube.ioi.domain.model;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Video video;

    public static Comment of(String contents) {
        Comment comment = new Comment();
        comment.contents = contents;

        return comment;
    }

    public void update(String contents) {
        this.contents = contents;
    }
}
