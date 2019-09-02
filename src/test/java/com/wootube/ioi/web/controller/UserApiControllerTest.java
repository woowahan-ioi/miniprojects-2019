package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.EmailCheckRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @DisplayName("중복된 이메일로 중복 요청시 message: impossible 반환")
    @Test
    void checkDuplicateDuplicatedEmail() {
        EmailCheckRequestDto requestDto = new EmailCheckRequestDto("a@test.com");

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(requestDto).
                when().
                post(baseUrl() + "/api/users").
                then().
                statusCode(200).
                body("message", equalTo("impossible"));
    }

    @DisplayName("중복되지 않은 이메일로 중복 요청시 message: possible 반환")
    @Test
    void checkDuplicateNonDuplicatedEmail() {
        EmailCheckRequestDto requestDto = new EmailCheckRequestDto("sanji@milzipmoza.com");

        given().
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(requestDto).
                when().
                post(baseUrl() + "/api/users").
                then().
                statusCode(200).
                body("message", equalTo("possible"));
    }
}