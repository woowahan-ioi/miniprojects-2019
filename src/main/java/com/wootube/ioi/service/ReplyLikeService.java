package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Reply;
import com.wootube.ioi.domain.model.ReplyLike;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.ReplyLikeRepository;
import com.wootube.ioi.service.dto.ReplyLikeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyLikeService {
    private final ReplyLikeRepository replyLikeRepository;

    private final UserService userService;
    private final CommentService commentService;
    private final ReplyService replyService;

    @Autowired
    public ReplyLikeService(ReplyLikeRepository replyLikeRepository, UserService userService, CommentService commentService, ReplyService replyService) {
        this.replyLikeRepository = replyLikeRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.replyService = replyService;
    }

    @Transactional
    public ReplyLikeResponseDto likeReply(Long userId, Long commentId, Long replyId) {
        commentService.findById(commentId);
        Reply reply = replyService.findById(replyId);
        User user = userService.findByIdAndIsActiveTrue(userId);
        ReplyLike replyLike = new ReplyLike(reply, user);

        if (replyLikeRepository.existsByLikeUserIdAndReplyId(userId, replyId)) {
            return getReplyLikeCount(replyId);
        }

        replyLikeRepository.save(replyLike);
        return getReplyLikeCount(replyId);
    }

    public ReplyLikeResponseDto getReplyLikeCount(Long replyId) {
        long count = replyLikeRepository.countByReplyId(replyId);
        return new ReplyLikeResponseDto(count);
    }

    @Transactional
    public ReplyLikeResponseDto dislikeReply(Long userId, Long commentId, Long replyId) {
        if (replyLikeRepository.existsByLikeUserIdAndReplyId(userId, replyId)) {
            commentService.findById(commentId);
            replyLikeRepository.deleteByLikeUserIdAndReplyId(userId, replyId);
            return getReplyLikeCount(replyId);
        }

        return getReplyLikeCount(replyId);
    }
}
