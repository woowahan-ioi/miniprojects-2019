package com.wootube.ioi.web.controller;

import com.wootube.ioi.repository.UserRepository;
import com.wootube.ioi.web.dto.SignUpRequestDto;

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

    private MultiValueMap<String, String> parser(SignUpRequestDto signUpRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", signUpRequestDto.getName());
        multiValueMap.add("email", signUpRequestDto.getEmail());
        multiValueMap.add("password", signUpRequestDto.getPassword());
        return multiValueMap;
    }
}