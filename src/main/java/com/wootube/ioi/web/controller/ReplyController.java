package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.ReplyService;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/watch/{videoId}/comments/{commentId}")
@Controller
public class ReplyController {
    private ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @ResponseBody
    @PostMapping("/replies")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long videoId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody ReplyRequestDto replyRequestDto) {
        //로그인 상태인가?
        //commentId가 존재하면 videoId랑 comment의 videoId가 같은가?
        return new ResponseEntity<>(replyService.save(replyRequestDto, commentId), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Void> updateReply(@PathVariable Long videoId,
                                            @PathVariable Long commentId,
                                            @PathVariable Long replyId,
                                            @RequestBody ReplyRequestDto replyRequestDto) {
        //로그인 상태인가?
        // 세션 유저와 답글 유저와 같은지 확인한다.
        //commentId가 존재하면 videoId랑 Comment의 videoId가 같은가?
        replyService.update(replyRequestDto, commentId, replyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long videoId,
                                            @PathVariable Long commentId,
                                            @PathVariable Long replyId) {
        // 로그인 상태인가?
        // 세션 유저와 답글 유저와 같은지 확인한다.
        //commentId가 존재하면 videoId랑 Comment의 videoId가 같은가?
        replyService.delete(commentId, replyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
