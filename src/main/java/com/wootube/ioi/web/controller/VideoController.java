package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.web.session.UserSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String video(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        Long userId = userSessionManager.getUserSession().getId();
        VideoResponseDto videoResponseDto = videoService.create(uploadFile, videoRequestDto, userId);
        return "redirect:/videos/" + videoResponseDto.getId();
    }

    @GetMapping("/{id}")
    public String video(@PathVariable Long id, Model model) {
        model.addAttribute("video", videoService.findById(id));
        return "video";
    }

    @GetMapping("/{id}/edit")
    public String updateVideoPage(@PathVariable Long id, Model model) {
        model.addAttribute("video", videoService.findById(id));
        return "video-edit";
    }

    @PutMapping("/{id}")
    public String updateVideo(@PathVariable Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        videoService.update(id, uploadFile, videoRequestDto);
        return "redirect:/videos/" + id;
    }

}
