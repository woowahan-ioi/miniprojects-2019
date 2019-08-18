package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.ReplyService;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/videos/{videoId}/comments/{commentId}")
@RestController
public class ReplyApiController {
    private ReplyService replyService;

    public ReplyApiController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/replies")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long videoId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody ReplyRequestDto replyRequestDto) {
        return new ResponseEntity<>(replyService.save(replyRequestDto, commentId), HttpStatus.CREATED);
    }

    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Void> updateReply(@PathVariable Long videoId,
                                            @PathVariable Long commentId,
                                            @PathVariable Long replyId,
                                            @RequestBody ReplyRequestDto replyRequestDto) {
        replyService.update(replyRequestDto, commentId, replyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long videoId,
                                            @PathVariable Long commentId,
                                            @PathVariable Long replyId) {
        replyService.delete(commentId, replyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
