package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequest;
import com.wootube.ioi.service.dto.CommentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = new Comment(commentRequest.getContents());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponse(updatedComment.getId(),
                updatedComment.getContents(),
                updatedComment.getUpdateTime());
    }
}
