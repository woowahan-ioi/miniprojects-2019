package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.LoginFailedException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserServiceTest extends TestUtil {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

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

    private User SAVED_USER = new User("루피", "luffy@luffy.com", "1234567a");

    @DisplayName("유저 등록 (회원 가입)")
    @Test
    void signUp() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");

        given(modelMapper.map(signUpRequestDto, User.class)).willReturn(SAVED_USER);

        userService.createUser(signUpRequestDto);

        verify(userRepository).save(SAVED_USER);
    }

    @DisplayName("유저 조회 (로그인 성공)")
    @Test
    void login() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "1234567a");
        User logInUser = userService.readUser(logInRequestDto);

        assertEquals(logInUser, SAVED_USER);
    }

    @DisplayName("유저 조회 (로그인 실패, 없는 아이디)")
    @Test
    void loginFailedNotFoundUser() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("notfound@luffy.com", "1234567a");
        assertThrows(LoginFailedException.class, () -> userService.readUser(logInRequestDto));
    }

    @DisplayName("유저 조회 (로그인 실패, 비밀번호 불일치)")
    @Test
    void loginFailedNotMatchPassword() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "aaaa1234");
        assertThrows(LoginFailedException.class, () -> userService.readUser(logInRequestDto));
    }

    @DisplayName("프로필 사진 업데이트")
    @Test
    void updateProfileImage() throws IOException {
        given(userRepository.findByIdAndActiveTrue(USER_ID)).willReturn(Optional.of(testUser));
        given(testUser.getProfileImage()).willReturn(PROFILE_IMAGE);
        given(fileConverter.convert(updateTestUploadFile)).willReturn(Optional.of(testFile));
        given(fileUploader.uploadFile(testFile, UploadType.PROFILE)).willReturn(PROFILE_IMAGE_URL);

        userService.updateProfileImage(USER_ID, updateTestUploadFile);

        verify(testFile).delete();
        verify(testUser).updateProfileImage(UPDATE_PROFILE_IMAGE);
    }
}