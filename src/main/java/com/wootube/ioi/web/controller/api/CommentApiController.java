package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RequestMapping("/api/videos/{videoId}/comments")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity createComment(HttpSession session, @PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto);
        return ResponseEntity.created(URI.create("/api/videos/" + videoId + "/comments/" + commentResponseDto.getId()))
                .body(commentResponseDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long videoId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto) {
        commentService.update(commentId, commentRequestDto);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent()
                .build();
    }
}
