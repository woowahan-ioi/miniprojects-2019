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
        Comment comment = validatorService.findComment(commentId, videoId);

        Reply savedReply = replyRepository.save(Reply.of(replyRequestDto.getContents(), comment, writer));
        return ReplyResponseDto.of(savedReply.getId(),
                savedReply.getContents(),
                savedReply.getUpdateTime());
    }

    @Transactional
    public ReplyResponseDto update(ReplyRequestDto replyRequestDto, String email, Long videoId, Long commentId, Long replyId) {
        User writer = validatorService.getUserService().findByEmail(email);
        Comment comment = validatorService.findComment(commentId, videoId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        reply.update(writer, comment, replyRequestDto.getContents());
        return ReplyResponseDto.of(reply.getId(),
                reply.getContents(),
                reply.getUpdateTime());
    }

    public void delete(Long videoId, Long commentId, Long replyId, String email) {
        User writer = validatorService.getUserService().findByEmail(email);
        Comment comment = validatorService.findComment(commentId, videoId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        reply.checkMatchWriter(writer);
        reply.checkMatchComment(comment);

        replyRepository.delete(reply);
    }
}
