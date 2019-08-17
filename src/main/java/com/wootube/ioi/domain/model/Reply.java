package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchCommentException;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String contents;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

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
