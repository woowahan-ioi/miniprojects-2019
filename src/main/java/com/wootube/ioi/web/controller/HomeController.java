package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	private final VideoService videoService;
	private final UserSessionManager userSessionManager;

	@Autowired
	public HomeController(VideoService videoService, UserSessionManager userSessionManager) {
		this.videoService = videoService;
		this.userSessionManager = userSessionManager;
	}

	@GetMapping("/")
	public String home(Model model) {
		UserSession userSession = userSessionManager.getUserSession();

		model.addAttribute("recommendVideos", videoService.findRecommendVideos());
		model.addAttribute("popularityVideos", videoService.findPopularityVideos());

		if (userSession != null) {
			model.addAttribute("subscribeVideos", videoService.findSubscribeVideos(userSession.getId()));
			return "index";
		}

		model.addAttribute("latestVideos", videoService.findLatestVideos());
		return "index";
	}
}
