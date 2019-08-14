package com.wootube.ioi.service;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.NotFoundUserException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User SAVED_USER = new User("루피", "luffy@luffy.com", "1234567a");

    @DisplayName("유저 등록 (회원 가입)")
    @Test
    void signUp() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");
        userService.signUp(signUpRequestDto);

        verify(userRepository, atLeast(1)).save(SAVED_USER);
    }

    @DisplayName("유저 조회 (로그인 성공)")
    @Test
    void login() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "1234567a");
        User logInUser = userService.login(logInRequestDto);

        assertEquals(logInUser, SAVED_USER);
    }

    @DisplayName("유저 조회 (로그인 실패, 없는 아이디)")
    @Test
    void loginFailedNotFoundUser() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("notfound@luffy.com", "1234567a");
        assertThrows(NotFoundUserException.class, () -> userService.login(logInRequestDto));
    }

    @DisplayName("유저 조회 (로그인 실패, 비밀번호 불일치)")
    @Test
    void loginFailedNotMatchPassword() {
        given(userRepository.findByEmail(SAVED_USER.getEmail())).willReturn(Optional.of(SAVED_USER));

        LogInRequestDto logInRequestDto = new LogInRequestDto("luffy@luffy.com", "aaaa1234");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(logInRequestDto));
    }
}