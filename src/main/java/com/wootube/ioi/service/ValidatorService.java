package com.wootube.ioi.service;

import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    private final UserService userService;
    private final VideoService videoService;
    private final CommentService commentService;
    private final ReplyService replyService;

    public ValidatorService(UserService userService, VideoService videoService, CommentService commentService, ReplyService replyService) {
        this.userService = userService;
        this.videoService = videoService;
        this.commentService = commentService;
        this.replyService = replyService;
    }

}
