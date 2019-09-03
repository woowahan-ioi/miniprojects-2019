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
public class ReplyLike extends BaseEntity {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_like_to_reply"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reply reply;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_reply_like_to_user"), nullable = false)
    private User likeUser;

    public ReplyLike(Reply reply, User likeUser) {
        this.reply = reply;
        this.likeUser = likeUser;
    }
}
