package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.CommentRepository;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final VideoService videoService;
    private final UserService userService;

    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, VideoService videoService, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.videoService = videoService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public CommentResponseDto save(CommentRequestDto commentRequestDto, Long videoId, String email) {
        User writer = userService.findByEmail(email);
        Video video = videoService.findById(videoId);
        Comment comment = commentRepository.save(Comment.of(commentRequestDto.getContents(), video, writer));
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    @Transactional
    public CommentResponseDto update(Long commentId, String email, Long videoId, CommentRequestDto commentRequestDto) {
        User writer = userService.findByEmail(email);
        Video video = videoService.findById(videoId);
        Comment comment = findById(commentId);

        comment.update(writer, video, commentRequestDto.getContents());
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public void delete(Long commentId, String email, Long videoId) {
        User writer = userService.findByEmail(email);
        Video video = videoService.findById(videoId);
        Comment comment = findById(commentId);

        comment.checkMatchVideo(video);
        comment.checkMatchWriter(writer);

        commentRepository.deleteById(commentId);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
    }

    public List<CommentResponseDto> sortComment(Sort sort, Long videoId) {
        List<Comment> comments = commentRepository.findAllByVideoId(sort, videoId);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        comments.forEach(comment -> {
            CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);
            commentResponseDtos.add(commentResponseDto);
        });
        return commentResponseDtos;
    }
}
