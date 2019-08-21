package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.web.session.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/videos")
@RestController
public class VideoApiController {
    private static final Logger log = LoggerFactory.getLogger(VideoApiController.class);
    private final VideoService videoService;
    private final UserSessionManager userSessionManager;

    public VideoApiController(VideoService videoService, UserSessionManager userSessionManager) {
        this.videoService = videoService;
        this.userSessionManager = userSessionManager;
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable Long videoId) {
//        UserSession userSession = userSessionManager.getUserSession();
//        videoService.deleteById(videoId, userSession.getId());
        videoService.deleteById(videoId);

        return ResponseEntity.noContent()
                .build();
    }
}
