package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.web.config.TestConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private S3Mock s3Mock;

    @Test
    @DisplayName("비디오 등록 페이지로 이동한다.")
    void moveCreateVideoPage() {
        request(HttpMethod.GET, "/videos/new")
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("비디오를 저장한다.")
    void save() {
        requestWithBodyBuilder(createMultipartBodyBuilder(), POST, "/videos/new")
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/videos/[1-9][0-9]*");

        stopS3Mock();
    }

    @Test
    @DisplayName("비디오를 조회한다.")
    void get() {
        String videoId = getVideoId(createMultipartBodyBuilder());

        request(HttpMethod.GET, "/videos/" + videoId)
                .expectStatus().isOk();

        stopS3Mock();
    }

    @Test
    @DisplayName("비디오 수정 페이지로 이동한다.")
    void moveEditPage() {
        String videoId = getVideoId(createMultipartBodyBuilder());

        request(HttpMethod.GET, "/videos/" + videoId + "/edit")
                .expectStatus().isOk();

        stopS3Mock();
    }

    @Test
    @DisplayName("등록 된 비디오를 수정한다.")
    void update() {
        MultipartBodyBuilder bodyBuilder = createMultipartBodyBuilder();
        String videoId = getVideoId(bodyBuilder);

        bodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
            @Override
            public String getFilename() {
                return "update_test_file.mp4";
            }

        }, MediaType.parseMediaType("video/mp4"));
        bodyBuilder.part("title", "update_video_title");
        bodyBuilder.part("description", "update_video_description");

        requestWithBodyBuilder(bodyBuilder, HttpMethod.PUT, "videos/" + videoId)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/videos/" + videoId);

        stopS3Mock();
    }

    @Test
    @DisplayName("등록 된 비디오를 삭제한다.")
    void delete() {
        String videoId = getVideoId(createMultipartBodyBuilder());

        request(HttpMethod.DELETE, "/api/videos/" + videoId)
                .expectStatus().isNoContent();

        stopS3Mock();
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

    private String getVideoId(MultipartBodyBuilder bodyBuilder) {
        String uri = saveVideo(bodyBuilder).toString();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    private WebTestClient.ResponseSpec request(HttpMethod requestMethod, String requestUri) {
        return webTestClient.method(requestMethod)
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange();
    }

    private WebTestClient.ResponseSpec requestWithBodyBuilder(MultipartBodyBuilder bodyBuilder, HttpMethod requestMethod, String requestUri) {
        signUpRequest();
        return webTestClient.method(requestMethod)
                .uri(requestUri)
                .header("Cookie", getLoginCookie(webTestClient, new LogInRequestDto("test@test.com", "1234qwer")))
                .body(BodyInserters.fromObject(bodyBuilder.build()))
                .exchange();
    }

    private URI saveVideo(MultipartBodyBuilder bodyBuilder) {
        return requestWithBodyBuilder(bodyBuilder, POST, "/videos/new")
                .returnResult(String.class)
                .getResponseHeaders()
                .getLocation();
    }

    private String getLoginCookie(WebTestClient webTestClient, LogInRequestDto logInRequestDto) {
        return webTestClient.post().uri("/user/login")
                .body(BodyInserters.fromFormData(parser(logInRequestDto)))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    private MultiValueMap<String, String> parser(LogInRequestDto logInRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", logInRequestDto.getEmail());
        multiValueMap.add("password", logInRequestDto.getPassword());
        return multiValueMap;
    }

    private WebTestClient.ResponseSpec signUpRequest() {
        return webTestClient.method(POST)
                .uri("/user/signup")
                .body(BodyInserters.fromFormData(parser(new SignUpRequestDto("루피", "test@test.com", "1234qwer"))))
                .exchange();
    }

    private MultiValueMap<String, String> parser(SignUpRequestDto signUpRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", signUpRequestDto.getName());
        multiValueMap.add("email", signUpRequestDto.getEmail());
        multiValueMap.add("password", signUpRequestDto.getPassword());
        return multiValueMap;
    }

}