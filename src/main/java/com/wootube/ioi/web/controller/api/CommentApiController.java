package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/api/videos")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{videoId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(HttpSession session, @PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.save(commentRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long videoId,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentRequestDto commentRequestDto) {
        commentService.update(commentId, commentRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
