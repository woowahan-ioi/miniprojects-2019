package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.UserService;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;
    private final UserSessionManager userSessionManager;

    @Autowired
    public UserController(UserService userService, UserSessionManager userSessionManager) {
        this.userService = userService;
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
    public String myPage() {
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
        userSessionManager.removeUserSession();
        return new RedirectView("/");
    }
}
