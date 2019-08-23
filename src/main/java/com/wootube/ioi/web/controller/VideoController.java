package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.dto.VideoResponseDto;
import com.wootube.ioi.web.controller.exception.InvalidUserException;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

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
        checkUserSession();
        return "video-edit";
    }

    @PostMapping("/new")
    public String video(MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        checkUserSession();
        VideoResponseDto videoResponseDto = videoService.create(uploadFile, videoRequestDto);
        return "redirect:/videos/" + videoResponseDto.getId();
    }

    private Long checkUserSession() {
        UserSession userSession = userSessionManager.getUserSession();
        if (userSession == null) {
            throw new InvalidUserException();
        }
        return userSession.getId();
    }

    @GetMapping("/{id}")
    public String video(@PathVariable Long id, Model model) {
        VideoResponseDto videoResponseDto = videoService.findVideo(id);
        model.addAttribute("video", videoResponseDto);
        return "video";
    }

    @GetMapping("/{id}/edit")
    public String updateVideoPage(@PathVariable Long id, Model model) {
        Long userId = checkUserSession();
        videoService.matchWriter(userId, id);
        model.addAttribute("video", videoService.findById(id));
        return "video-edit";
    }

    @PutMapping("/{id}")
    public String updateVideo(@PathVariable Long id, MultipartFile uploadFile, VideoRequestDto videoRequestDto) {
        checkUserSession();
        videoService.update(id, uploadFile, videoRequestDto);
        return "redirect:/videos/" + id;
    }
}
