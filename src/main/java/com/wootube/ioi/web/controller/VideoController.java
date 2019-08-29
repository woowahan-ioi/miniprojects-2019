package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RequestMapping("/videos")
@Controller
public class VideoController {
    private final VideoService videoService;
    private final UserSessionManager userSessionManager;

    public VideoController(VideoService videoService, UserSessionManager userSessionManager) {
        this.videoService = videoService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping("/new")
    public String createVideo() {
        return "video-edit";
    }

    @PostMapping("/new")
    public RedirectView video(MultipartFile uploadFile, VideoRequestDto videoRequestDto) throws IOException {
        VideoResponseDto videoResponseDto = videoService.create(uploadFile, videoRequestDto);
        return new RedirectView("/videos/" + videoResponseDto.getId());
    }

    @GetMapping("/{id}")
    public String video(@PathVariable Long id, Model model) {
        VideoResponseDto videoResponseDto = videoService.findVideo(id);
        model.addAttribute("video", videoResponseDto);
        model.addAttribute("videos", videoService.findAll());

        return "video";
    }

    @GetMapping("/{id}/edit")
    public String updateVideoPage(@PathVariable Long id, Model model) {
        UserSession userSession = userSessionManager.getUserSession();
        Long userId = userSession.getId();
        videoService.matchWriter(userId, id);
        model.addAttribute("video", videoService.findVideo(id));
        return "video-edit";
    }

    @PutMapping("/{id}")
    public RedirectView updateVideo(@PathVariable Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) throws IOException {
        videoService.update(id, uploadFile, videoRequestDto);
        return new RedirectView("/videos/" + id);
    }
}
