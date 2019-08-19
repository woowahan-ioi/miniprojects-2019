package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchCommentException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reply extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne
    private Comment comment;

    public Reply(String contents, Comment comment) {
        this.contents = contents;
        this.comment = comment;
    }

    public void update(Comment comment, String contents) {
        checkMatchComment(comment);
        this.contents = contents;
    }

    public void checkMatchComment(Comment comment) {
        if (!this.comment.equals(comment)) {
            throw new NotMatchCommentException();
        }
    }
}
