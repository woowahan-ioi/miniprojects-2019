package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @DisplayName("로그인 폼 페이지로 이동")
    @Test
    void loginForm() {
        webTestClient.get().uri("/user/login")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("회원가입 폼 페이지로 이동")
    @Test
    void signUpForm() {
        webTestClient.get().uri("/user/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("로그인 하지 않고 마이 페이지로 이동")
    @Test
    void mypage() {
        webTestClient.get().uri("/user/mypage")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @DisplayName("회원등록")
    @Test
    void signUp() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");

        webTestClient.post().uri("/user/signup")
                .body(BodyInserters.fromFormData(parser(signUpRequestDto)))
                .exchange()
                .expectStatus()
                .isFound();

        assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("회원조회 (로그인 성공)")
    @Test
    void login() {
        userRepository.save(new User("루피", "luffy@luffy.com", "1234567a"));

        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "1234567a");

        webTestClient.post().uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @DisplayName("회원조회 (로그인 실패)")
    @Test
    void loginFailedNoUser() {
        userRepository.save(new User("루피", "luffy@luffy.com", "1234567a"));

        LogInRequestDto logInRequestDto = new LogInRequestDto("nono@luffy.com", "1234567a");

        webTestClient.post().uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    private MultiValueMap<String, String> parser(SignUpRequestDto signUpRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", signUpRequestDto.getName());
        multiValueMap.add("email", signUpRequestDto.getEmail());
        multiValueMap.add("password", signUpRequestDto.getPassword());
        return multiValueMap;
    }

    private MultiValueMap<String, String> parser(LogInRequestDto logInRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", logInRequestDto.getEmail());
        multiValueMap.add("password", logInRequestDto.getPassword());
        return multiValueMap;
    }
}