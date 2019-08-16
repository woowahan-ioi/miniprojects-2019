package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.CommentRequestDto;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentCommonControllerTest {
    static final Long EXIST_COMMENT_ID = 1L;
    static final Long EXIST_REPLY_ID = 1L;
    static final Long NOT_EXIST_COMMENT_ID = 0L;
    static final Long NOT_EXIST_REPLY_ID = 0L;

    static final CommentResponseDto SAVE_COMMENT_RESPONSE = new CommentResponseDto(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now());
    static final CommentResponseDto UPDATE_COMMENT_RESPONSE = new CommentResponseDto(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());
    static final ReplyResponseDto SAVE_REPLY_RESPONSE = new ReplyResponseDto(1L,
            "Reply Contents",
            LocalDateTime.now());

    static final String NOT_FOUND_COMMENT_EXCEPTION_MESSAGE = "존재하지 않는 댓글 입니다.";
    static final String NOT_FOUND_REPLY_EXCEPTION_MESSAGE = "존재하지 않는 답글 입니다.";


    @Autowired
    protected WebTestClient webTestClient;


    WebTestClient.ResponseSpec loginAndSaveComment() {
//        로그인한다.
//        String loginSessionId = login();

        return webTestClient.post()
                .uri("/watch/1/comments")
                //.cookie("JSESSIONID", loginSessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new CommentRequestDto(SAVE_COMMENT_RESPONSE.getContents())), CommentRequestDto.class)
                .exchange();
    }
}
