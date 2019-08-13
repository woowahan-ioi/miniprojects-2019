package com.wootube.ioi.service;

import com.wootube.ioi.domain.User;
import com.wootube.ioi.repository.UserRepository;
import com.wootube.ioi.web.dto.SignUpRequestDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}