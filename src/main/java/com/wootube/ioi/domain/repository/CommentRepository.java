package com.wootube.ioi.domain.repository;

import java.util.List;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Video;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByVideo(Sort sort, Video video);
}
