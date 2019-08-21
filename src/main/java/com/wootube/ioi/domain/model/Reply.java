package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchCommentException;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_to_comment"), nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_to_user"), nullable = false)
    private User writer;

    public static Reply of(String contents, Comment comment, User writer) {
        Reply reply = new Reply();
        reply.contents = contents;
        reply.comment = comment;
        reply.writer = writer;
        return reply;
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
