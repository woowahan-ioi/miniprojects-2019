package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.web.TestConfig;
import io.findify.s3mock.S3Mock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    S3Mock s3Mock;

    @Test
    @DisplayName("비디오를 저장한다.")
    void save() {
        String expected = "mock1.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected, "image/png", "mock date".getBytes());

        webTestClient.post().uri("/videos/new")
                .contentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE))
                .syncBody(mockMultipartFile)
                .exchange()
                .expectStatus()
                .is3xxRedirection();
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("version", "1.0");
//        RestAssured.given(this.spec).accept("application/json")
//                .filter(document("image-upload", requestPartBody("metadata")))
//                .when().multiPart("image", new File("image.png"), "image/png")
//                .multiPart("metadata", metadata).post("images")
//                .then().assertThat().statusCode(200);
    }

    @AfterEach
    void tearDown() {
        s3Mock.stop();
    }
}