package com.wootube.ioi.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class IndexControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("로그인 폼 페이지로 이동")
    @Test
    void loginForm() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }
}