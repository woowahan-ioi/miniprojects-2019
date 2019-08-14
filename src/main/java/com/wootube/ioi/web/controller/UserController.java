package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.User;
import com.wootube.ioi.service.UserService;
import com.wootube.ioi.web.dto.LogInRequestDto;
import com.wootube.ioi.web.dto.SignUpRequestDto;
import com.wootube.ioi.web.session.LoginUserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/user")
@Controller
public class UserController {

    private UserService userService;
    private LoginUserSessionManager loginUserSessionManager;

    @Autowired
    public UserController(UserService userService, LoginUserSessionManager loginUserSessionManager) {
        this.userService = userService;
        this.loginUserSessionManager = loginUserSessionManager;
    }

    @GetMapping("/signup")
    public String createUserFrom() {
        return "signup";
    }

    @GetMapping("/login")
    public String createLoginFrom() {
        return "login";
    }

    @PostMapping("/signup")
    public RedirectView signUp(SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return new RedirectView("/user/login");
    }

    @PostMapping("/login")
    public RedirectView login(LogInRequestDto logInRequestDto) {
        User loginUser = userService.login(logInRequestDto);
        loginUserSessionManager.setUser(loginUser);
        return new RedirectView("/");
    }
}
