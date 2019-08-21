package com.wootube.ioi.web.controller;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wootube.ioi.service.dto.*;

import com.wootube.ioi.web.config.TestConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import static io.restassured.RestAssured.*;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonControllerTest {
    static final Long EXIST_COMMENT_ID = 1L;
    static final Long NOT_EXIST_COMMENT_ID = 0L;
    static final Long NOT_EXIST_REPLY_ID = 0L;
    static final Long NOT_EXIST_VIDEO_ID = 0L;

    static final CommentResponseDto SAVE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now());
    static final CommentResponseDto UPDATE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());
    static final ReplyResponseDto SAVE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Reply Contents",
            LocalDateTime.now());

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private S3Mock s3Mock;

    String basicPath() {
        return "http://localhost:" + port;
    }

    int getCommentId2(String sessionId, String videoId) {
        return  given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    cookie("JSESSIONID", sessionId).
                    body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/"+ videoId +"/comments").
                    getBody().
                    jsonPath().
                    get("id");
    }

    int getCommentId() {
        return given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/1/comments").
                    getBody().
                    jsonPath().
                    get("id");
    }

    int getReplyId() {
        int commentId = getCommentId();

        return given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/1/comments/" + commentId + "/replies").
                    getBody().
                    jsonPath().
                    get("id");
    }

    int getReplyId(int commentId) {
        return given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    body(ReplyRequestDto.of(SAVE_REPLY_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/1/comments/" + commentId + "/replies").
                    getBody().
                    jsonPath().
                    get("id");
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

    private WebTestClient.ResponseSpec requestWithBodyBuilder(MultipartBodyBuilder bodyBuilder, HttpMethod requestMethod, String requestUri, String sessionId) {
        return webTestClient.method(requestMethod)
                .uri(requestUri)
                .cookie("JSESSIONID", sessionId)
                .body(BodyInserters.fromObject(bodyBuilder.build()))
                .exchange();
    }

    private MultipartBodyBuilder createMultipartBodyBuilder() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
            @Override
            public String getFilename() {
                return "test_file.mp4";
            }
        }, MediaType.parseMediaType("video/mp4"));
        bodyBuilder.part("title", "video_title");
        bodyBuilder.part("description", "video_description");
        return bodyBuilder;
    }

    private void stopS3Mock() {
        s3Mock.stop();
    }

    void signup() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");

        webTestClient.post().uri("/user/signup")
                .body(BodyInserters.fromFormData(parser(signUpRequestDto)))
                .exchange()
                .expectStatus()
                .isFound();
    }

    String login() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "1234567a");

        return webTestClient.post().uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }


    void saveVideo(String sessionId) {
        requestWithBodyBuilder(createMultipartBodyBuilder(), HttpMethod.POST, "/videos/new", sessionId)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/videos/[1-9][0-9]*");

        stopS3Mock();
    }

    String getSaveVideoId(String sessionId) {
        String videoId = requestWithBodyBuilder(createMultipartBodyBuilder(), HttpMethod.POST, "/videos/new", sessionId)
                .expectStatus().is3xxRedirection()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("location");

        stopS3Mock();
        return videoId.substring(videoId.length() - 1);
    }
}
