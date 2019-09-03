package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class FileServiceTest extends TestUtil {
    @InjectMocks
    private FileService fileService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileConverter fileConverter;

    @Mock
    private FileUploader fileUploader;

    @Mock
    private User testUser;

    @Mock
    private File testFile;

    private MultipartFile updateTestUploadFile;

    @BeforeEach
    void setUp() {
        updateTestUploadFile = new MockMultipartFile(PROFILE_IMAGE_URL, UPDATE_PROFILE_IMAGE_FILE_NAME, "image/png", CONTENTS.getBytes(StandardCharsets.UTF_8));
    }

    @DisplayName("프로필 사진 업데이트")
    @Test
    void updateProfileImage() throws IOException {
        given(userService.findByIdAndIsActiveTrue(USER_ID)).willReturn(testUser);
        given(userRepository.findByIdAndActiveTrue(USER_ID)).willReturn(Optional.of(testUser));
        given(testUser.getProfileImage()).willReturn(PROFILE_IMAGE);
        given(fileConverter.convert(updateTestUploadFile)).willReturn(testFile);
        given(fileUploader.uploadFile(testFile, UploadType.PROFILE)).willReturn(PROFILE_IMAGE_URL);

        fileService.updateProfileImage(USER_ID, updateTestUploadFile);

        verify(testFile).delete();
        verify(testUser).updateProfileImage(UPDATE_PROFILE_IMAGE);
    }
}