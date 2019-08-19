package com.wootube.ioi.web.controller;

import java.net.URI;

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
import org.springframework.web.reactive.function.BodyInserters;

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
        requestWithBodyBuilder(createMultipartBodyBuilder(), HttpMethod.POST, "/videos/new")
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

        request(HttpMethod.DELETE, "/videos/" + videoId)
                .expectHeader().valueMatches("Location", ".*/");

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
                .exchange();
    }

    private WebTestClient.ResponseSpec requestWithBodyBuilder(MultipartBodyBuilder bodyBuilder, HttpMethod requestMethod, String requestUri) {
        return webTestClient.method(requestMethod)
                .uri(requestUri)
                .body(BodyInserters.fromObject(bodyBuilder.build()))
                .exchange();
    }

    private URI saveVideo(MultipartBodyBuilder bodyBuilder) {
        return requestWithBodyBuilder(bodyBuilder, HttpMethod.POST, "/videos/new")
                .returnResult(String.class)
                .getResponseHeaders()
                .getLocation();
    }
}