package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;

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

import javax.servlet.http.HttpSession;

@RequestMapping("/api/videos")
@Controller
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @PostMapping("/{videoId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(HttpSession session, @PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(commentService.save(commentRequestDto), HttpStatus.CREATED);
    }

    @ResponseBody
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
