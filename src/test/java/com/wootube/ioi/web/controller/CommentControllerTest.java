package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.CommentRequest;
import com.wootube.ioi.service.dto.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final CommentResponse SAVE_COMMENT_RESPONSE = new CommentResponse(1L,
            "Comment Contents",
            LocalDateTime.now());

    private static final CommentResponse UPDATE_COMMENT_RESPONSE = new CommentResponse(1L,
            "Update Contents",
            LocalDateTime.now());

    @Test
    public void createComment() {
//        로그인한다.
//        String loginSessionId = login();

        saveComment()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo(SAVE_COMMENT_RESPONSE.getContents())
                .jsonPath("$.id").isEqualTo(SAVE_COMMENT_RESPONSE.getId())
                .jsonPath("$.updateTime").isNotEmpty()
        ;
    }

    @Test
    void updateComment() {
        saveComment();

        webTestClient.put()
                .uri("/watch/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequest(UPDATE_COMMENT_RESPONSE.getContents())), CommentRequest.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    private String login() {
        User user = new User("hyo", "hyo@naver.com", "password123!");

        return webTestClient.post()
                .uri("/login")
                .body(Mono.just(user), User.class)
                .exchange()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }

    private WebTestClient.ResponseSpec saveComment() {
        return webTestClient.post()
                .uri("/watch/1/comments")
                //.cookie("JSESSIONID", loginSessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequest(SAVE_COMMENT_RESPONSE.getContents())), CommentRequest.class)
                .exchange();
    }
}
