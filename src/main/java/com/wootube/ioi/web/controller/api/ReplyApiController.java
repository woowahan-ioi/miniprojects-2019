package com.wootube.ioi.web.controller.api;

import java.net.URI;

import com.wootube.ioi.service.ReplyService;
import com.wootube.ioi.service.dto.ReplyRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;

import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/videos/{videoId}/comments/{commentId}/replies")
@RestController
public class ReplyApiController {
    private final ReplyService replyService;
    private final UserSessionManager userSessionManager;

    public ReplyApiController(ReplyService replyService, UserSessionManager userSessionManager) {
        this.replyService = replyService;
        this.userSessionManager = userSessionManager;
    }

    @PostMapping
    public ResponseEntity createReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        ReplyResponseDto replyResponseDto = replyService.save(replyRequestDto, commentId, userSession.getEmail(), videoId);
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentId + "/replies/" + replyResponseDto.getId()))
                .body(replyResponseDto);
    }

    @PutMapping("/{replyId}")
    public ResponseEntity updateReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId,
                                      @RequestBody ReplyRequestDto replyRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        replyService.update(replyRequestDto, userSession.getEmail(), videoId, commentId, replyId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity deleteReply(@PathVariable Long videoId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long replyId) {
        UserSession userSession = userSessionManager.getUserSession();
        replyService.delete(videoId, commentId, replyId, userSession.getEmail());
        return ResponseEntity.noContent()
                .build();
    }
}
