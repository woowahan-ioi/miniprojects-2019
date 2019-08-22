package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Comment;
import com.wootube.ioi.domain.model.Video;
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

    public Comment findComment(Long commentId, Long videoId) {
        Video video = videoService.findVideo(videoId);
        Comment comment = commentService.findById(commentId);

        comment.checkMatchVideo(video);
        return comment;
    }
}
