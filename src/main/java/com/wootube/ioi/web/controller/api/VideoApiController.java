package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.VideoLikeService;
import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/videos")
@RestController
public class VideoApiController {
    private final VideoService videoService;
    private final VideoLikeService videoLikeService;
    private final UserSessionManager userSessionManager;

    @Autowired
    public VideoApiController(VideoService videoService, VideoLikeService videoLikeService, UserSessionManager userSessionManager) {
        this.videoService = videoService;
        this.videoLikeService = videoLikeService;
        this.userSessionManager = userSessionManager;
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable Long videoId) {
        UserSession userSession = userSessionManager.getUserSession();
        videoService.deleteById(videoId, userSession.getId());

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{videoId}/likes")
    public ResponseEntity like(@PathVariable Long videoId) {
        UserSession userSession = userSessionManager.getUserSession();
        return ResponseEntity.ok(videoLikeService.likeVideo(videoId, userSession.getId()));
    }

    @GetMapping("/{videoId}/likes/counts")
    public ResponseEntity count(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoLikeService.getVideoLikeCount(videoId));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<VideoResponseDto>> subscriptions() {
        List<VideoResponseDto> subscribeVideos = videoService.findSubscribeVideos();
        return ResponseEntity.ok(subscribeVideos);
    }

    @GetMapping("/populars")
    public ResponseEntity<List<VideoResponseDto>> populars() {
        List<VideoResponseDto> popularVideos = videoService.findPopularityVideos();
        return ResponseEntity.ok(popularVideos);
    }
}
