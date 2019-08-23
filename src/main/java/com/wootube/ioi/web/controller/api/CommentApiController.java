package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/videos/{videoId}/comments")
@RestController
public class CommentApiController {
    private final CommentService commentService;
    private final UserSessionManager userSessionManager;

    public CommentApiController(CommentService commentService, UserSessionManager userSessionManager) {
        this.commentService = commentService;
        this.userSessionManager = userSessionManager;
    }

    @PostMapping
    public ResponseEntity createComment(@PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto, videoId, userSession.getEmail());
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentResponseDto.getId()))
                .body(commentResponseDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long videoId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        commentService.update(commentId, userSession.getEmail(), videoId, commentRequestDto);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        UserSession userSession = userSessionManager.getUserSession();
        commentService.delete(commentId, userSession.getEmail(), videoId);
        return ResponseEntity.noContent()
                .build();
    }
}
