package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.CommentLike;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.CommentLikeRepository;
import com.wootube.ioi.service.dto.CommentLikeResponseDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    private final UserService userService;
    private final VideoService videoService;
    private final CommentService commentService;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository, UserService userService, VideoService videoService, CommentService commentService) {
        this.commentLikeRepository = commentLikeRepository;
        this.userService = userService;
        this.videoService = videoService;
        this.commentService = commentService;
    }

    public boolean existCommentLike(Long userId, Long commentId) {
        return commentLikeRepository.existsByLikeUserIdAndCommentId(userId, commentId);
    }

    @Transactional
    public CommentLikeResponseDto likeComment(Long userId, Long commentId, Long videoId) {
        videoService.findById(videoId);
        Comment comment = commentService.findById(commentId);
        User user = userService.findByIdAndIsActiveTrue(userId);
        CommentLike commentLike = new CommentLike(comment, user);

        if (existCommentLike(userId, commentId)) {
            return getCommentLikeCount(commentId);
        }

        commentLikeRepository.save(commentLike);
        return getCommentLikeCount(commentId);
    }

    public CommentLikeResponseDto getCommentLikeCount(Long commentId) {
        long count = commentLikeRepository.countByCommentId(commentId);
        return new CommentLikeResponseDto(count);
    }

    public List<CommentResponseDto> saveCommentLike(List<CommentResponseDto> comments) {
        comments.forEach(commentResponseDto -> {
            long commentId = commentResponseDto.getId();
            commentResponseDto.setLike(commentLikeRepository.countByCommentId(commentId));
        });
        return comments;
    }

    @Transactional
    public CommentLikeResponseDto unlikeComment(Long userId, Long commentId, Long videoId) {
        if (existCommentLike(userId, commentId)) {
            videoService.findById(videoId);
            commentLikeRepository.deleteByLikeUserIdAndCommentId(userId, commentId);
            return getCommentLikeCount(commentId);
        }

        return getCommentLikeCount(commentId);
    }
}
