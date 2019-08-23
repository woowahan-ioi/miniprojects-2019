package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.UserService;
import com.wootube.ioi.service.VideoService;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;
    private final VideoService videoService;
    private final UserSessionManager userSessionManager;

    public UserController(UserService userService, VideoService videoService, UserSessionManager userSessionManager) {
        this.userService = userService;
        this.videoService = videoService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping("/signup")
    public String createUserFrom() {
        return "signup";
    }

    @GetMapping("/login")
    public String createLoginFrom() {
        return "login";
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {
        UserSession userSession = userSessionManager.getUserSession();
        if (userSession.getId() != null) {
            model.addAttribute("videos", videoService.findAllByWriter(userSession.getId()));
        }
        return "mypage";
    }

    @PostMapping("/signup")
    public RedirectView signUp(SignUpRequestDto signUpRequestDto) {
        userService.createUser(signUpRequestDto);
        return new RedirectView("/user/login");
    }

    @PostMapping("/login")
    public RedirectView login(LogInRequestDto logInRequestDto) {
        User loginUser = userService.readUser(logInRequestDto);
        userSessionManager.setUserSession(loginUser);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        userSessionManager.removeUserSession();
        return new RedirectView("/");
    }

    @PutMapping("/")
    public RedirectView editUser(EditUserRequestDto editUserRequestDto) {
        UserSession userSession = userSessionManager.getUserSession();
        User updatedUser = userService.updateUser(userSession.getId(), editUserRequestDto);
        userSessionManager.setUserSession(updatedUser);
        return new RedirectView("/user/mypage");
    }

    @DeleteMapping("/")
    public RedirectView deleteUser() {
        UserSession userSession = userSessionManager.getUserSession();
        userService.deleteUser(userSession.getId());
        return logout();
    }

    @GetMapping("/confirm")
    public RedirectView user(@RequestParam String email, @RequestParam String verifyKey) {
        userService.activateUser(email, verifyKey);
        return new RedirectView("/user/login");
    }
}
