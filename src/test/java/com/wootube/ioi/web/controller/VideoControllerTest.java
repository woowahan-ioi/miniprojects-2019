package com.wootube.ioi.web.controller;

import java.net.URI;

import com.wootube.ioi.service.dto.LogInRequestDto;
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

import static org.springframework.http.HttpMethod.*;

@AutoConfigureWebTestClient
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoControllerTest extends CommonControllerTest {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private S3Mock s3Mock;

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
	void save() {
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
			@Override
			public String getFilename() {
				return "test_file.mp4";
			}
		}, MediaType.parseMediaType("video/mp4"));

		bodyBuilder.part("title", "video_title");
		bodyBuilder.part("description", "video_description");
		bodyBuilder.part("userId", 1);

		requestWithBodyBuilder(bodyBuilder, POST, "/videos/new")
				.expectStatus().isFound();
		stopS3Mock();
	}

	@Test
	@DisplayName("비디오를 조회한다.")
	void get() {
		LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
		loginAndRequest(GET, "/videos/1", logInRequestDto)
				.expectStatus().isFound();
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
				.expectStatus().isFound()
				.expectHeader().valueMatches("Location", ".*/");
		stopS3Mock();
	}

	@Test
	@DisplayName("등록 된 비디오를 수정한다.")
	void update() {
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
			@Override
			public String getFilename() {
				return "test_file.mp4";
			}
		}, MediaType.parseMediaType("video/mp4"));
		bodyBuilder.part("title", "video_title");
		bodyBuilder.part("description", "video_description");
		bodyBuilder.part("userId", 1);

		requestWithBodyBuilder(bodyBuilder, POST, "/videos/new");

		MultipartBodyBuilder updateBodyBuilder = new MultipartBodyBuilder();
		updateBodyBuilder.part("uploadFile", new ByteArrayResource(new byte[]{1, 2, 3, 4}) {
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
	void delete() {
		String videoId = getVideoId(createMultipartBodyBuilder());
		LogInRequestDto logInRequestDto = new LogInRequestDto("a@test.com", "1234qwer");
		loginAndRequest(DELETE, "/api/videos/" + videoId, logInRequestDto)
				.expectStatus().isNoContent();
		stopS3Mock();
	}

	private void stopS3Mock() {
		s3Mock.stop();
	}

	private String getVideoId(MultipartBodyBuilder bodyBuilder) {
		String uri = saveVideo(bodyBuilder).toString();
		return uri.substring(uri.lastIndexOf("/") + 1);
	}

	private WebTestClient.ResponseSpec requestWithBodyBuilder(MultipartBodyBuilder bodyBuilder, HttpMethod requestMethod, String requestUri) {
		return webTestClient.method(requestMethod)
				.uri(requestUri)
				.header("Cookie", getLoginCookie(webTestClient, new LogInRequestDto("a@test.com", "1234qwer")))
				.body(BodyInserters.fromObject(bodyBuilder.build()))
				.exchange();
	}

	private URI saveVideo(MultipartBodyBuilder bodyBuilder) {
		URI uri = requestWithBodyBuilder(bodyBuilder, POST, "/videos/new")
				.returnResult(String.class)
				.getResponseHeaders()
				.getLocation();
		return uri;
	}

	private String getLoginCookie(WebTestClient webTestClient, LogInRequestDto logInRequestDto) {
		return webTestClient.post().uri("/user/login")
				.body(BodyInserters.fromFormData(parser(logInRequestDto)))
				.exchange()
				.returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
	}

	public MultiValueMap<String, String> parser(LogInRequestDto logInRequestDto) {
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("email", logInRequestDto.getEmail());
		multiValueMap.add("password", logInRequestDto.getPassword());
		return multiValueMap;
	}
}