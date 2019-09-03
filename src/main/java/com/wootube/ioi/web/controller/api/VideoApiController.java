package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.VideoLikeService;
import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/videos")
@RestController
@Slf4j
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
    public ResponseEntity<List<VideoResponseDto>> subscriptions(@PageableDefault(size = 12) Pageable pageable) {
        UserSession userSession = userSessionManager.getUserSession();
        List<VideoResponseDto> subscribeVideos = videoService.findSubscribeVideos(pageable, userSession.getId());
        return ResponseEntity.ok(subscribeVideos);
    }

    @GetMapping("/populars")
    public ResponseEntity<List<VideoResponseDto>> populars(@PageableDefault(size = 12, sort = "views", direction = Sort.Direction.DESC) Pageable pageable) {
        List<VideoResponseDto> popularVideos = videoService.findPopularityVideos(pageable);
        return ResponseEntity.ok(popularVideos);
    }

    @GetMapping("/latests")
    public ResponseEntity<List<VideoResponseDto>> latests(@PageableDefault(size = 12, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<VideoResponseDto> popularVideos = videoService.findLatestVideos(pageable);
        if (popularVideos.get(0).getCreateTime() != null) {
            log.error("시간 : " + popularVideos.get(0).getCreateTime());
        }
        return ResponseEntity.ok(popularVideos);
    }

    @GetMapping("/recommends")
    public ResponseEntity<List<VideoResponseDto>> recommends(@PageableDefault(size = 12, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<VideoResponseDto> popularVideos = videoService.findRecommendVideos(pageable);
        return ResponseEntity.ok(popularVideos);
    }
}
