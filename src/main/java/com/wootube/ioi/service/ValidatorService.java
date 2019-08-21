package com.wootube.ioi.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ValidatorService {
    private final UserService userService;
    private final VideoService videoService;
    private final CommentService commentService;

    public ValidatorService(UserService userService, VideoService videoService, CommentService commentService) {
        this.userService = userService;
        this.videoService = videoService;
        this.commentService = commentService;
    }
}
