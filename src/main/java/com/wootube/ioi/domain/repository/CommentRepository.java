package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
