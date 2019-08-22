package com.wootube.ioi.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpMethod.GET;

class ErrorControllerTest extends CommonControllerTest {

    @DisplayName("에러 URL 리다이렉트 정상")
    @Test
    void errorRedirection() {
        request(GET, "/errors/unknown")
                .expectStatus().isFound();
    }
}