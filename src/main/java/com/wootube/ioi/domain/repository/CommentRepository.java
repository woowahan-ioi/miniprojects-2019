package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByVideoId(Sort sort, Long videoId);
}
