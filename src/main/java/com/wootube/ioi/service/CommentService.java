package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.exception.NotFoundCommentException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    public CommentResponseDto save(CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.save(Comment.of(commentRequestDto.getContents()));
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    @Transactional
    public CommentResponseDto update(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = findById(commentId);

        comment.update(commentRequestDto.getContents());
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public void delete(Long commentId) {
        Comment comment = findById(commentId);
        commentRepository.deleteById(commentId);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
    }
}
