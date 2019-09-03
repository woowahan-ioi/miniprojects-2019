package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Reply;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByComment(Sort sort, Comment comment);
}
