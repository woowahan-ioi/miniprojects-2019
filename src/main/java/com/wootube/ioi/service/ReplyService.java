package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Reply;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.ReplyRepository;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import com.wootube.ioi.service.exception.NotFoundReplyException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final ValidatorService validatorService;

    public ReplyService(ReplyRepository replyRepository, ValidatorService validatorService) {
        this.replyRepository = replyRepository;
        this.validatorService = validatorService;
    }

    public ReplyResponseDto save(ReplyRequestDto replyRequestDto, Long commentId, String email, Long videoId) {
        User writer = validatorService.getUserService().findByEmail(email);
        Video video = validatorService.getVideoService().findVideo(videoId);
        Comment comment = validatorService.getCommentService().findById(commentId);

        comment.checkMatchVideo(video);

        Reply savedReply = replyRepository.save(Reply.of(replyRequestDto.getContents(), comment, writer));
        return ReplyResponseDto.of(savedReply.getId(),
                savedReply.getContents(),
                savedReply.getUpdateTime());
    }

    @Transactional
    public ReplyResponseDto update(ReplyRequestDto replyRequestDto, String email, Long videoId, Long commentId, Long replyId) {
        User writer = validatorService.getUserService().findByEmail(email);
        Video video = validatorService.getVideoService().findVideo(videoId);
        Comment comment = validatorService.getCommentService().findById(commentId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        comment.checkMatchVideo(video);
        reply.update(writer, comment, replyRequestDto.getContents());
        return ReplyResponseDto.of(reply.getId(),
                reply.getContents(),
                reply.getUpdateTime());
    }

    public void delete(Long videoId, Long commentId, Long replyId, String email) {
        User writer = validatorService.getUserService().findByEmail(email);
        Video video = validatorService.getVideoService().findVideo(videoId);
        Comment comment = validatorService.getCommentService().findById(commentId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        comment.checkMatchVideo(video);
        reply.checkMatchWriter(writer);
        reply.checkMatchComment(comment);

        replyRepository.delete(reply);
    }
}
