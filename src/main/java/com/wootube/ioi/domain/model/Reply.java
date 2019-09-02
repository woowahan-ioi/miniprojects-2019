package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchCommentException;
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
public class Reply extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_to_comment"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public void update(User writer, Comment comment, String contents) {
        checkMatchWriter(writer);
        checkMatchComment(comment);
        this.contents = contents;
    }

    public void checkMatchWriter(User writer) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }
    }

    public void checkMatchComment(Comment comment) {
        if (!this.comment.equals(comment)) {
            throw new NotMatchCommentException();
        }
    }
}
