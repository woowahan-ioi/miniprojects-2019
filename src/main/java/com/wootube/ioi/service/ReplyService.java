package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Reply;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.ReplyRepository;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import com.wootube.ioi.service.exception.NotFoundReplyException;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final VideoService videoService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    public ReplyService(ReplyRepository replyRepository, UserService userService, VideoService videoService, CommentService commentService, ModelMapper modelMapper) {
        this.replyRepository = replyRepository;
        this.userService = userService;
        this.videoService = videoService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    public ReplyResponseDto save(ReplyRequestDto replyRequestDto, Long commentId, String email, Long videoId) {
        User writer = userService.findByEmail(email);
        Comment comment = findComment(commentId, videoId);

        Reply savedReply = replyRepository.save(Reply.of(replyRequestDto.getContents(), comment, writer));
        return modelMapper.map(savedReply, ReplyResponseDto.class);
    }

    @Transactional
    public ReplyResponseDto update(ReplyRequestDto replyRequestDto, String email, Long videoId, Long commentId, Long replyId) {
        User writer = userService.findByEmail(email);
        Comment comment = findComment(commentId, videoId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        reply.update(writer, comment, replyRequestDto.getContents());
        return modelMapper.map(reply, ReplyResponseDto.class);

    }

    public void delete(Long videoId, Long commentId, Long replyId, String email) {
        User writer = userService.findByEmail(email);
        Comment comment = findComment(commentId, videoId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        reply.checkMatchWriter(writer);
        reply.checkMatchComment(comment);

        replyRepository.delete(reply);
    }

    private Comment findComment(Long commentId, Long videoId) {
        Video video = videoService.findVideo(videoId);
        Comment comment = commentService.findById(commentId);

        comment.checkMatchVideo(video);
        return comment;
    }
}
