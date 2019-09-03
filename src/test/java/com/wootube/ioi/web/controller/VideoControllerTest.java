package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.dto.LogInRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import static org.springframework.http.HttpMethod.*;

class VideoControllerTest extends CommonControllerTest {
    @Test
    @DisplayName("비디오 등록 페이지로 이동한다.")
    void moveCreateVideoPage() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
        loginAndRequest(HttpMethod.GET, "/videos/new", logInRequestDto)
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("권한이 없는 경우, 비디오 등록 페이지가 아닌 로그인 페이지로 이동한다.")
    void canNotMoveCreateVideoPage() {
        request(HttpMethod.GET, "/videos/new")
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("비디오를 저장한다.")
    void save() throws IOException {
        File file = ResourceUtils.getFile("classpath:test_file.mp4");
        byte[] fileContent = Files.readAllBytes(file.toPath());

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("uploadFile", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "test_file.mp4";
            }
        }, MediaType.parseMediaType("video/mp4"));
        bodyBuilder.part("title", "video_title");
        bodyBuilder.part("description", "video_description");
        bodyBuilder.part("writerId", 1);

        requestWithBodyBuilder(bodyBuilder, POST, "/videos/new")
                .expectStatus().isFound();
        stopS3Mock();
    }

    @Test
    @DisplayName("비디오를 조회한다.")
    void get() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
        loginAndRequest(GET, "/videos/2", logInRequestDto)
                .expectStatus().isOk();
        stopS3Mock();
    }

    @Test
    @DisplayName("비디오 수정 페이지로 이동한다.")
    void moveEditPage() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("b@test.com", "1234qwer");
        loginAndRequest(GET, "/videos/2/edit", logInRequestDto)
                .expectStatus().isOk();
        stopS3Mock();
    }

    @Test
    @DisplayName("권한이 없는 경우, 비디오 수정 페이지가 아닌 메인 페이지로 이동한다.")
    void canNotMoveEditPage() {
        LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
        loginAndRequest(GET, "/videos/2/edit", logInRequestDto)
                .expectStatus().isFound();
        stopS3Mock();
    }

    @Test
    @DisplayName("등록 된 비디오를 수정한다.")
    void update() throws IOException {
        MultipartBodyBuilder bodyBuilder = createMultipartBodyBuilder();
        requestWithBodyBuilder(bodyBuilder, POST, "/videos/new");

        File file = ResourceUtils.getFile("classpath:update_test_file.mp4");
        byte[] fileContent = Files.readAllBytes(file.toPath());

        MultipartBodyBuilder updateBodyBuilder = new MultipartBodyBuilder();
        updateBodyBuilder.part("uploadFile", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "update_test_file.mp4";
            }
        }, MediaType.parseMediaType("video/mp4"));
        updateBodyBuilder.part("title", "update_video_title");
        updateBodyBuilder.part("description", "update_video_description");
        updateBodyBuilder.part("userId", 1);

        requestWithBodyBuilder(updateBodyBuilder, PUT, "/videos/4")
                .expectStatus().isFound();

        stopS3Mock();
    }

    @Test
    @DisplayName("등록 된 비디오를 삭제한다.")
    void delete() throws IOException {
        String videoId = getVideoId(createMultipartBodyBuilder());
        LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
        loginAndRequest(DELETE, "/api/videos/" + videoId, logInRequestDto)
                .expectStatus().isNoContent();
        stopS3Mock();
    }

    private String getVideoId(MultipartBodyBuilder bodyBuilder) {
        String uri = saveVideo(bodyBuilder).toString();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    private URI saveVideo(MultipartBodyBuilder bodyBuilder) {
        return requestWithBodyBuilder(bodyBuilder, POST, "/videos/new")
                .returnResult(String.class)
                .getResponseHeaders()
                .getLocation();
    }

    MultipartBodyBuilder createMultipartBodyBuilder() throws IOException {
        File file = ResourceUtils.getFile("classpath:test_file.mp4");
        byte[] fileContent = Files.readAllBytes(file.toPath());

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("uploadFile", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "test_file.mp4";
            }
        }, MediaType.parseMediaType("video/mp4"));
        bodyBuilder.part("title", "video_title");
        bodyBuilder.part("description", "video_description");
        bodyBuilder.part("writerId", 1);
        return bodyBuilder;
    }
}