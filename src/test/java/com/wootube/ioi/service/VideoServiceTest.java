package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.model.Video;
import com.wootube.ioi.domain.repository.VideoRepository;
import com.wootube.ioi.service.dto.VideoRequestDto;
import com.wootube.ioi.service.testutil.TestUtil;
import com.wootube.ioi.service.util.FileConverter;
import com.wootube.ioi.service.util.FileUploader;
import com.wootube.ioi.service.util.UploadType;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class VideoServiceTest extends TestUtil {
    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserService userService;

    @Mock
    private FileUploader fileUploader;

    @Mock
    private FileConverter fileConverter;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Video testVideo;

    @InjectMocks
    private VideoService videoService;

    private VideoRequestDto testVideoRequestDto;
    private MultipartFile testUploadMultipartFile;

    private String videoFileFullPath;
    private String thumbnailImageFileFullPath;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = mock(User.class);

        videoFileFullPath = String.format("%s/%s/%s", DIRECTORY, UploadType.VIDEO, VIDEO_FILE_NAME);
        thumbnailImageFileFullPath = String.format("%s/%s/%s", DIRECTORY, UploadType.PROFILE, THUMBNAIL_FILE_NAME);

        testUploadMultipartFile = new MockMultipartFile(videoFileFullPath, VIDEO_FILE_NAME, null, CONTENTS.getBytes(StandardCharsets.UTF_8));

        testVideoRequestDto = new VideoRequestDto(TITLE, DESCRIPTION);
    }

    @Test
    @DisplayName("서비스에서 비디오를 저장한다.")
    void create() throws IOException {
        createMockVideo();

        verify(modelMapper, atLeast(1)).map(testVideoRequestDto, Video.class);
        verify(videoRepository, atLeast(1)).save(testVideo);
    }

    private void createMockVideo() throws IOException {
        File convertedVideo = mock(File.class);
        File convertedThumbnail = mock(File.class);

        mockUploadVideo(convertedVideo, convertedThumbnail);

        given(modelMapper.map(testVideoRequestDto, Video.class)).willReturn(testVideo);
        given(userService.findByIdAndIsActiveTrue(USER_ID)).willReturn(writer);

        videoService.create(testUploadMultipartFile, testVideoRequestDto, USER_ID);
    }

    private void mockUploadVideo(File convertedVideo, File convertedThumbnail) {
        given(fileConverter.convert(testUploadMultipartFile)).willReturn(convertedVideo);
        given(fileUploader.uploadFile(convertedVideo, UploadType.VIDEO)).willReturn(videoFileFullPath);

        given(fileConverter.convert(convertedVideo)).willReturn(convertedThumbnail);
        given(fileUploader.uploadFile(convertedThumbnail, UploadType.THUMBNAIL)).willReturn(thumbnailImageFileFullPath);
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
        testVideo = mock(Video.class);
        given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
        given(testVideo.matchWriter(any())).willReturn(true);

        File convertedVideo = mock(File.class);
        File convertedThumbnail = mock(File.class);

        mockUploadVideo(convertedVideo, convertedThumbnail);

        videoService.update(ID, testUploadMultipartFile, testVideoRequestDto, USER_ID);

        verify(testVideo).updateVideo(videoFileFullPath, convertedVideo.getName(), thumbnailImageFileFullPath, convertedThumbnail.getName());
        verify(testVideo).updateTitle(testVideoRequestDto.getTitle());
        verify(testVideo).updateDescription(testVideoRequestDto.getDescription());
    }

    @Test
    @DisplayName("비디오를 삭제한다.")
    void deleteById() {
        deleteMockVideo();
        videoService.deleteById(ID, WRITER.getId());

        verify(fileUploader).deleteFile(testVideo.getOriginFileName(), UploadType.VIDEO);
        verify(fileUploader).deleteFile(testVideo.getThumbnailFileName(), UploadType.THUMBNAIL);
        verify(videoRepository).deleteById(ID);
    }

    private void deleteMockVideo() {
        given(testVideo.getId()).willReturn(ID);
        given(testVideo.matchWriter(any())).willReturn(true);
        given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
    }

    @Test
    @DisplayName("비디오의 조회수를 높인다.")
    void increaseVideo() {
        given(videoRepository.findById(ID)).willReturn(Optional.of(testVideo));
        videoService.findVideo(ID);

        verify(videoRepository).findById(ID);
        verify(testVideo).increaseViews();
    }
}