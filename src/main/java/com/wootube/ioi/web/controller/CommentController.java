package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/watch")
@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @PostMapping("/{videoId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(HttpSession session, @PathVariable Long videoId, @RequestBody CommentRequestDto commentRequestDto) {
        //로그인 상태인가?
        return new ResponseEntity<>(commentService.save(commentRequestDto), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long videoId,
                                                         @PathVariable Long commentId,
                                                         @RequestBody CommentRequestDto commentRequestDto) {
        //로그인 상태인가?
        //세션 유저와 댓글 유저와 같은지 확인한다.
        commentService.update(commentId, commentRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long videoId, @PathVariable Long commentId) {
        // 로그인 상태인가?
        // 세션 유저와 댓글 유저와 같은지 확인한다.
        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
