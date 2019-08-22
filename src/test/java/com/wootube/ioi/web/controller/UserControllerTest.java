package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(SpringExtension.class)
public class UserControllerTest extends CommonControllerTest {

    @DisplayName("로그인 폼 페이지로 이동")
    @Test
    void loginForm() {
        request(GET, "/user/login")
                .expectStatus().isOk();
    }

    @DisplayName("회원가입 폼 페이지로 이동")
    @Test
    void signUpForm() {
        request(GET, "/user/signup")
                .expectStatus().isOk();
    }

    @DisplayName("로그인 하지 않고 마이 페이지로 이동")
    @Test
    void mypage() {
        request(GET, "/user/mypage")
                .expectStatus().isFound();
    }

    @DisplayName("회원등록")
    @Test
    void signUp() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy1@luffy.com", "1234567a");

        request(POST, "/user/signup", parser(signUpRequestDto))
                .expectStatus().isFound();
    }

    @DisplayName("회원조회 (로그인 성공)")
    @Test
    void login() {
        request(POST, "/user/login", parser(USER_A_LOGIN_REQUEST_DTO))
                .expectStatus().isFound();
    }

    @DisplayName("회원조회 (로그인 실패)")
    @Test
    void loginFailedNoUser() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("nono@luffy.com", "1234567a");

        request(POST, "/user/login", parser(logInRequestDto))
                .expectStatus().is5xxServerError();
    }
}