package com.wootube.ioi.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpMethod.GET;

class HomeControllerTest extends CommonControllerTest {

    @DisplayName("home 페이지로 이동")
    @Test
    void home() {
        request(GET, "/")
                .expectStatus().isOk();
    }
}