package com.wootube.ioi.domain.repository;


import com.wootube.ioi.domain.model.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    long countByReplyId(Long replyId);

    void deleteByLikeUserIdAndReplyId(Long userId, Long replyId);

    boolean existsByLikeUserIdAndReplyId(Long userId, Long replyId);
}