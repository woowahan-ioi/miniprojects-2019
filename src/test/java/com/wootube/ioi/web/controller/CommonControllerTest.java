package com.wootube.ioi.web.controller;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.CommentResponseDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.ReplyResponseDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.web.config.TestConfig;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;
import java.time.LocalDateTime;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonControllerTest {
    public static final LogInRequestDto USER_A_LOGIN_REQUEST_DTO = new LogInRequestDto("a@test.com", "1234qwer");
    public static final LogInRequestDto USER_B_LOGIN_REQUEST_DTO = new LogInRequestDto("b@test.com", "1234qwer");
    public static final LogInRequestDto USER_D_LOGIN_REQUEST_DTO = new LogInRequestDto("d@test.com", "1234qwer");
    static final Long USER_A_ID = 1L;
    static final Long USER_B_ID = 2L;
    static final Long USER_C_ID = 3L;
    static final Long USER_D_ID = 4L;
    static final Long EXIST_COMMENT_ID = 1L;
    static final Long NOT_EXIST_COMMENT_ID = 0L;
    static final Long NOT_EXIST_REPLY_ID = 0L;
    static final Long NOT_EXIST_VIDEO_ID = 0L;
    static final Long USER_A_VIDEO_ID = 1L;
    static final Long USER_B_VIDEO_ID = 2L;
    static final Long USER_C_VIDEO_ID = 3L;
    static final Long USER_A_VIDEO_USER_A_COMMENT = 1L;
    static final Long USER_A_VIDEO_USER_B_COMMENT = 2L;
    static final Long USER_B_VIDEO_USER_A_COMMENT = 3L;
    static final Long USER_B_VIDEO_USER_B_COMMENT = 4L;
    static final SignUpRequestDto SIGN_UP_COMMON_REQUEST_DTO = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");
    static final LogInRequestDto LOG_IN_COMMON_REQUEST_DTO = new LogInRequestDto("luffy@luffy.com", "1234567a");
    static final User SIGN_UP_USER = new User(SIGN_UP_COMMON_REQUEST_DTO.getName(), SIGN_UP_COMMON_REQUEST_DTO.getEmail(), SIGN_UP_COMMON_REQUEST_DTO.getPassword());
    static final CommentResponseDto SAVE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Comment Contents",
            LocalDateTime.now(), SIGN_UP_USER.getName(), "");
    static final CommentResponseDto UPDATE_COMMENT_RESPONSE = CommentResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now(), SIGN_UP_USER.getName(), "");
    static final ReplyResponseDto SAVE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Reply Contents",
            LocalDateTime.now(), SIGN_UP_USER.getName(), "");
    static final ReplyResponseDto UPDATE_REPLY_RESPONSE = ReplyResponseDto.of(EXIST_COMMENT_ID,
            "Update Contents",
            LocalDateTime.now(), SIGN_UP_USER.getName(), "");
    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private S3Mock s3Mock;


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

    public MultiValueMap<String, String> parser(LogInRequestDto logInRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", logInRequestDto.getEmail());
        multiValueMap.add("password", logInRequestDto.getPassword());
        return multiValueMap;
    }

    public MultiValueMap<String, String> parser(SignUpRequestDto signUpRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", signUpRequestDto.getName());
        multiValueMap.add("email", signUpRequestDto.getEmail());
        multiValueMap.add("password", signUpRequestDto.getPassword());
        return multiValueMap;
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

    WebTestClient.ResponseSpec loginAndRequest(HttpMethod method, String uri, MultiValueMap<String, String> data, LogInRequestDto logInRequestDto) {
        String sessionValue = login(logInRequestDto);
        return webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(15000))
                .build().method(method)
                .uri(uri)
                .cookie("JSESSIONID", sessionValue)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    WebTestClient.ResponseSpec loginAndRequest(HttpMethod method, String uri, LogInRequestDto logInRequestDto) {
        return loginAndRequest(method, uri, new LinkedMultiValueMap<>(), logInRequestDto);
    }

    WebTestClient.ResponseSpec requestWithBodyBuilder(MultipartBodyBuilder bodyBuilder, HttpMethod requestMethod, String requestUri) {
        return webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(15000))
                .build()
                .method(requestMethod)
                .uri(requestUri)
                .header("Cookie", getLoginCookie(webTestClient, new LogInRequestDto("a@test.com", "1234qwer")))
                .body(BodyInserters.fromObject(bodyBuilder.build()))
                .exchange();
    }

    String getLoginCookie(WebTestClient webTestClient, LogInRequestDto logInRequestDto) {
        return webTestClient.post().uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    void stopS3Mock() {
        s3Mock.stop();
    }
}