package com.wootube.ioi.web.controller.api;

import java.net.URI;

import com.wootube.ioi.service.ReplyService;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/videos/{videoId}/comments/{commentId}/replies")
@RestController
public class ReplyApiController {
    private final ReplyService replyService;

    public ReplyApiController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping
    public ResponseEntity createReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        ReplyResponseDto replyResponseDto = replyService.save(replyRequestDto, commentId);
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyResponseDto.getId()))
                .body(replyResponseDto);
    }

    @PutMapping("/{replyId}")
    public ResponseEntity updateReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        replyService.update(replyRequestDto, commentId, replyId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity deleteReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId) {
        replyService.delete(commentId, replyId);
        return ResponseEntity.noContent()
                .build();
    }
}
