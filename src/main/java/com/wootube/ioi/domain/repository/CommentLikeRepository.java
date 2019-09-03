package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    long countByCommentId(Long commentId);

    void deleteByLikeUserIdAndCommentId(Long userId, Long commentId);

    boolean existsByLikeUserIdAndCommentId(Long userId, Long commentId);
}
