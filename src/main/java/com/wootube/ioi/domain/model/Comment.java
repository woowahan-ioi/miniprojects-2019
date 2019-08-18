package com.wootube.ioi.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String contents;

    public static Comment of(String contents) {
        Comment comment = new Comment();
        comment.contents = contents;

        return comment;
    }

    public void update(String contents) {
        this.contents = contents;
    }
}
