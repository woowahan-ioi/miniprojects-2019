package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Video;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
class VideoServiceTest {
    private static final Long ID = 1L;
    private static final String DIRECTORY = "wootube";
    @Mock
    private VideoRepository videoRepository;

    @Mock
    private FileUploader fileUploader;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VideoService videoService;

    private VideoRequestDto testVideoRequestDto;
    private Video testVideo;
    private MultipartFile testUploadFile;
    private String fileFullPath;

    @BeforeEach
    void setUp() {
        String fileName = "testVideo.mp4";
        fileFullPath = String.format("%s/%s", DIRECTORY, fileName);

        testUploadFile = new MockMultipartFile(fileFullPath, fileName, null, "<<testVideo data>>".getBytes(StandardCharsets.UTF_8));

        testVideoRequestDto = new VideoRequestDto();
        testVideoRequestDto.setTitle("title");
        testVideoRequestDto.setDescription("description");

        testVideo = new Video();
        testVideo.setTitle(testVideoRequestDto.getTitle());
        testVideo.setDescription(testVideoRequestDto.getDescription());
    }

    @Test
    @DisplayName("서비스에서 비디오 저장 테스트를 한다.")
    void create() throws IOException {
        given(fileUploader.uploadFile(testUploadFile, DIRECTORY)).willReturn(fileFullPath);
        given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);

        videoService.create(testUploadFile, testVideoRequestDto);

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
    void update() throws IOException {
        String fileName = "changeTestVideo.mp4";
        String updateFileFullPath = String.format("%s/%s", DIRECTORY, fileName);

        MultipartFile testUpdateChangeUploadFile = new MockMultipartFile(fileFullPath, fileName, null, "<<testVideo data>>".getBytes(StandardCharsets.UTF_8));

        testVideoRequestDto.setTitle("update_title");
        testVideoRequestDto.setDescription("update_description");
        testVideo.setTitle(testVideoRequestDto.getTitle());
        testVideo.setDescription(testVideoRequestDto.getDescription());

        given(fileUploader.uploadFile(testUploadFile, DIRECTORY)).willReturn(fileFullPath);
        given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);

        given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
        given(fileUploader.uploadFile(testUpdateChangeUploadFile, DIRECTORY)).willReturn(updateFileFullPath);

        videoService.create(testUploadFile, testVideoRequestDto);

        videoService.update(ID, testUpdateChangeUploadFile, testVideoRequestDto);

        verify(modelMapper, atLeast(1)).map(testVideoRequestDto, Video.class);
        verify(videoRepository, atLeast(1)).save(testVideo);
    }

    @Test
    @DisplayName("서비스에서 비디오 아이디로 비디오를 삭제한다.")
    void deleteById() throws IOException {
        given(fileUploader.uploadFile(testUploadFile, DIRECTORY)).willReturn(fileFullPath);
        given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);
        videoService.create(testUploadFile, testVideoRequestDto);

        given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
        videoService.deleteById(ID);

        verify(fileUploader, atLeast(1)).deleteFile(DIRECTORY, testVideo.getOriginFileName());
        verify(videoRepository, atLeast(1)).deleteById(ID);
    }
}