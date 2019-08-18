package com.wootube.ioi.domain.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Comment {
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

//    @ManyToOne
//    @JoinColumn
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User writer;
//
//    @ManyToOne
//    @JoinColumn
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Video video;

    public static Comment of(String contents) {
        Comment comment = new Comment();
        comment.contents = contents;

        return comment;
    }

    //public void update(String contents, User writer, Video video)와 같이 수정
    public void update(String contents) {
        //댓글 작성자와 세션 유저가 같은지 확인한다.
        //댓글의 비디오와 요청하는 비디오가 같은지 확인한다.
        this.contents = contents;
    }
}
