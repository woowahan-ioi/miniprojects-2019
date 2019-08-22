package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.*;
import com.wootube.ioi.web.config.TestConfig;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonControllerTest {
    static final Long EXIST_COMMENT_ID = 1L;
    static final Long NOT_EXIST_COMMENT_ID = 0L;
    static final Long NOT_EXIST_REPLY_ID = 0L;
    static final Long NOT_EXIST_VIDEO_ID = 0L;

    static final SignUpRequestDto SIGN_UP_COMMON_REQUEST_DTO = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");
    static final LogInRequestDto LOG_IN_COMMON_REQUEST_DTO = new LogInRequestDto("luffy@luffy.com", "1234567a");
    static final SignUpRequestDto SIGN_UP_SECOND_USER_DTO = new SignUpRequestDto("효오", "hyo@test.com", "1234qwer");
    static final LogInRequestDto LOG_IN_SECOND_USER_DTO = new LogInRequestDto("hyo@test.com", "1234qwer");


    static final CommentResponseDto SAVE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now());
    static final CommentResponseDto UPDATE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());
    static final ReplyResponseDto SAVE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Reply Contents",
            LocalDateTime.now());
    static final ReplyResponseDto UPDATE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now());

    public static final LogInRequestDto USER_A_LOGIN_REQUEST_DTO = new LogInRequestDto("a@test.com", "1234qwer");
    public static final LogInRequestDto USER_B_LOGIN_REQUEST_DTO = new LogInRequestDto("b@test.com", "1234qwer");

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private S3Mock s3Mock;

    private void stopS3Mock() {
        s3Mock.stop();
    }

    String basicPath() {
        return "http://localhost:" + port;
    }

    public WebTestClient.ResponseSpec request(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    public WebTestClient.ResponseSpec request(HttpMethod method, String uri) {
        return request(method, uri, new LinkedMultiValueMap<>());
    }

    public WebTestClient.ResponseSpec loginAndRequest(HttpMethod method, String uri, MultiValueMap<String, String> data, LogInRequestDto logInRequestDto) {
        String sessionValue = login(logInRequestDto);

        return webTestClient.method(method)
                .uri(uri)
                .cookie("JSESSIONID", sessionValue)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    public WebTestClient.ResponseSpec loginAndRequest(HttpMethod method, String uri,LogInRequestDto logInRequestDto) {
        return loginAndRequest(method, uri, new LinkedMultiValueMap<>(), logInRequestDto);
    }

    public MultiValueMap<String, String> parser(SignUpRequestDto signUpRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", signUpRequestDto.getName());
        multiValueMap.add("email", signUpRequestDto.getEmail());
        multiValueMap.add("password", signUpRequestDto.getPassword());
        return multiValueMap;
    }

    public MultiValueMap<String, String> parser(LogInRequestDto logInRequestDto) {
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

    void signup(SignUpRequestDto signUpRequestDto) {
        webTestClient.post()
                .uri("/user/signup")
                .body(BodyInserters.fromFormData(parser(signUpRequestDto)))
                .exchange();
    }

    String login(LogInRequestDto logInRequestDto) {
        return webTestClient.post()
                .uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }

    String getSavedVideoId(String sessionId) {
        String videoId = requestWithBodyBuilder(createMultipartBodyBuilder(), HttpMethod.POST, "/videos/new", sessionId)
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("location");

        stopS3Mock();
        String[] urlValue = videoId.split("/");
        return urlValue[urlValue.length - 1];
    }

    int getSavedCommentId(String sessionId, String videoId) {
        return given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    cookie("JSESSIONID", sessionId).
                    body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/" + videoId + "/comments").
                    getBody().
                    jsonPath().
                    get("id");
    }

    int getSavedReplyId(String videoId, int commentId, String sessionId) {
        return given().
                    contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                    cookie("JSESSIONID", sessionId).
                    body(CommentRequestDto.of(SAVE_COMMENT_RESPONSE.getContents())).
                when().
                    post(basicPath() + "/api/videos/" + videoId + "/comments/" + commentId + "/replies").
                    getBody().
                    jsonPath().
                    get("id");
    }
}
