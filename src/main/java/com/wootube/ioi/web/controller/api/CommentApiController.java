package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;

import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/videos")
@RestController
public class CommentApiController {
    private final CommentService commentService;
    private final UserSessionManager userSessionManager;

    public CommentApiController(CommentService commentService, UserSessionManager userSessionManager) {
        this.commentService = commentService;
        this.userSessionManager = userSessionManager;
    }

    @PostMapping("/{videoId}/comments")
    public ResponseEntity createComment( @PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto, videoId, userSession.getEmail());
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentResponseDto.getId()))
                .body(commentResponseDto);
    }

    @PutMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long videoId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto) {
        commentService.update(commentId, commentRequestDto);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent()
                .build();
    }
}
