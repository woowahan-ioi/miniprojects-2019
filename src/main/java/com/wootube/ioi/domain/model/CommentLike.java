package com.wootube.ioi.domain.model;


import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
public class CommentLike extends BaseEntity {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_like_to_comment"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_like_to_user"), nullable = false)
    private User likeUser;

    public CommentLike(Comment comment, User likeUser) {
        this.comment = comment;
        this.likeUser = likeUser;
    }
}
