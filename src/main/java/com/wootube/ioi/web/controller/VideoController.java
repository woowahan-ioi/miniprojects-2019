package com.wootube.ioi.web.controller;

import com.wootube.ioi.request.VideoDto;
import com.wootube.ioi.service.VideoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/videos")
@Controller
public class VideoController {
	private final VideoService videoService;

	public VideoController(final VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/new")
	public String createVideo() {
		return "video-edit";
	}

	@PostMapping("/new")
	public String video(MultipartFile uploadFile, VideoDto videoDto) {
		videoService.create(uploadFile, videoDto);
		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String video(@PathVariable Long id, Model model) {
		model.addAttribute("video", videoService.findById(id));
		return "video";
	}
}
