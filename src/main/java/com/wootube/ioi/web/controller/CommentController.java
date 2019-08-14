package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.CommentService;
import com.wootube.ioi.service.dto.CommentRequest;
import com.wootube.ioi.service.dto.CommentResponse;
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
    public ResponseEntity<CommentResponse> createComment(HttpSession session, @PathVariable Long videoId, @RequestBody CommentRequest commentRequest) {
        //로그인 상태인가?
        return new ResponseEntity<>(commentService.save(commentRequest), HttpStatus.CREATED);
    }
}
