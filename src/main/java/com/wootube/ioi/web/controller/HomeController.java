package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final VideoService videoService;

    public HomeController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("videos", videoService.findAll());
        model.addAttribute("orderVideos", videoService.findOrderVideos());
        model.addAttribute("popularityVideos", videoService.findPopularityVideos());
        return "index";
    }
}
