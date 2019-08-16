package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.NotMatchCommentException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
        if (!this.comment.equals(comment)) {
            throw new NotMatchCommentException();
        }
        this.contents = contents;
    }
}
