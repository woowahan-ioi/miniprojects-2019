package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.EmailCheckResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class UserSignUpServiceTest {
    private static final User SAVED_USER = new User("루피", "luffy@luffy.com", "1234567a");
    private static final String NOT_FOUND_USER_EMAIL = "notfound@luffy.com";

    @InjectMocks
    private UserSignUpService userSignUpService;

    @Mock
    private UserService userService;


    @DisplayName("이메일 중복 체크 (중복되지 않은 이메일)")
    @Test
    void checkDuplicatedNotDuplicated() {
        given(userService.findByEmail(NOT_FOUND_USER_EMAIL)).willThrow(NotFoundUserException.class);

        EmailCheckResponseDto responseDto = userSignUpService.checkDuplicate(NOT_FOUND_USER_EMAIL);
        assertEquals(EmailCheckResponseDto.possible().getMessage(), responseDto.getMessage());
    }

    @DisplayName("이메일 중복 체크 (중복된 이메일)")
    @Test
    void checkDuplicatedDuplicated() {
        given(userService.findByEmail(SAVED_USER.getEmail())).willReturn(SAVED_USER);

        EmailCheckResponseDto responseDto = userSignUpService.checkDuplicate(SAVED_USER.getEmail());
        assertEquals(EmailCheckResponseDto.impossible().getMessage(), responseDto.getMessage());
    }
}