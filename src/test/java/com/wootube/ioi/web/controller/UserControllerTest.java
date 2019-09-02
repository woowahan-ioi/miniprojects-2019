package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;

import static org.springframework.http.HttpMethod.*;

public class UserControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("[로그인 X] 로그인 페이지로 이동")
    void loginForm() {
        request(GET, "/user/login")
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("[로그인 O] 로그인 페이지로 이동")
    void loginFormAfterLogin() {
        loginAndRequest(GET, "/user/login", USER_A_LOGIN_REQUEST_DTO)
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("[로그인 X] 회원가입 페이지로 이동")
    void signUpForm() {
        request(GET, "/user/signup")
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("[로그인 O] 회원가입 페이지로 이동")
    void signUpFormAfterLogin() {
        loginAndRequest(GET, "/user/signup", USER_A_LOGIN_REQUEST_DTO)
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("[로그인 X] 마이 페이지로 이동")
    void mypage() {
        request(GET, "/user/mypage")
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("[로그인 O] 마이 페이지로 이동")
    void mypageAfterLogin() {
        loginAndRequest(GET, "/user/mypage", USER_A_LOGIN_REQUEST_DTO)
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("회원등록")
    void signUp() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy1@luffy.com", "1234567a");

        request(POST, "/user/signup", parser(signUpRequestDto))
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("회원조회 (로그인 성공)")
    void login() {
        request(POST, "/user/login", parser(USER_A_LOGIN_REQUEST_DTO))
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("회원조회 (로그인 실패)")
    void loginFailedNoUser() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("nono@luffy.com", "1234567a");

        request(POST, "/user/login", parser(logInRequestDto))
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("프로필사진 수정")
    void editProfileImage() {
        MultipartBodyBuilder updateBodyBuilder = new MultipartBodyBuilder();
        updateBodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
            @Override
            public String getFilename() {
                return "update_test_image.png";
            }
        }, MediaType.parseMediaType("image/png"));

        requestWithBodyBuilder(updateBodyBuilder, PUT, "/user/images")
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", basicPath() + "/user/mypage");
        stopS3Mock();
    }
}