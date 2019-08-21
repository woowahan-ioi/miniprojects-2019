package com.wootube.ioi.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.util.FileUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class VideoServiceTest {
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String CONTENTS = "<<testVideo data>>";
	private static final String UPDATE_TITLE = "title";
	private static final String UPDATE_DESCRIPTION = "description";
	private static final String UPDATE_CONTENTS = "<<update testVideo data>>";
	private static final Long USER_ID = 1L;

	private static final Long ID = 1L;
	private static final String DIRECTORY = "wootube";
	private static final String FILE_NAME = "testVideo.mp4";
	private static final String UPDATE_FILE_NAME = "changeTestVideo.mp4";

	@Mock
	private VideoRepository videoRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private FileUploader fileUploader;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private Video testVideo;

	@InjectMocks
	private VideoService videoService;

	private VideoRequestDto testVideoRequestDto;

	private MultipartFile testUploadFile;
	private String fileFullPath;
	private User writer;

	@BeforeEach
	void setUp() {
		writer = new User();
		fileFullPath = String.format("%s/%s", DIRECTORY, FILE_NAME);

		testUploadFile = new MockMultipartFile(fileFullPath, FILE_NAME, null, CONTENTS.getBytes(StandardCharsets.UTF_8));

		testVideoRequestDto = new VideoRequestDto();
		testVideoRequestDto.setTitle(TITLE);
		testVideoRequestDto.setDescription(DESCRIPTION);
	}

	@Test
	@DisplayName("서비스에서 비디오 저장 테스트를 한다.")
	void create() {
		given(fileUploader.uploadFile(testUploadFile)).willReturn(fileFullPath);
		given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(writer));

		videoService.create(testUploadFile, testVideoRequestDto, USER_ID);

		verify(modelMapper, atLeast(1)).map(testVideoRequestDto, Video.class);
		verify(videoRepository, atLeast(1)).save(testVideo);
	}

	@Test
	@DisplayName("서비스에서 비디오 id를 통해 비디오를 찾는다.")
	void findById() {
		given(modelMapper.map(videoRepository.findById(ID), Video.class)).willReturn(testVideo);
		verify(videoRepository, atLeast(1)).findById(ID);
	}

	@Test
	@DisplayName("서비스에서 비디오를 업데이트 한다.")
	void update() {
		given(fileUploader.uploadFile(testUploadFile)).willReturn(fileFullPath);
		given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(writer));

		videoService.create(testUploadFile, testVideoRequestDto, USER_ID);

		String updateFileFullPath = String.format("%s/%s", DIRECTORY, UPDATE_FILE_NAME);
		MultipartFile testUpdateChangeUploadFile = getUpdateChangeUploadFile(updateFileFullPath);

		given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
		given(fileUploader.uploadFile(testUpdateChangeUploadFile)).willReturn(updateFileFullPath);

		videoService.update(ID, testUpdateChangeUploadFile, testVideoRequestDto);

		verify(modelMapper, atLeast(1)).map(testVideoRequestDto, Video.class);
		verify(videoRepository, atLeast(1)).save(testVideo);
	}

	private MultipartFile getUpdateChangeUploadFile(String updateFileFullPath) {
		testVideoRequestDto.setTitle(UPDATE_TITLE);
		testVideoRequestDto.setDescription(UPDATE_DESCRIPTION);

		return new MockMultipartFile(updateFileFullPath, UPDATE_FILE_NAME, null, UPDATE_CONTENTS.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("서비스에서 비디오 아이디로 비디오를 삭제한다.")
	void deleteById() {
		given(testVideo.getId()).willReturn(ID);
		given(testVideo.matchWriter(USER_ID)).willReturn(testVideo);
		given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
//		videoService.deleteById(ID, USER_ID);
		videoService.deleteById(ID);

		verify(fileUploader, atLeast(1)).deleteFile(testVideo.getOriginFileName());
		verify(videoRepository, atLeast(1)).deleteById(ID);
	}
}