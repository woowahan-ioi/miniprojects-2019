package com.wootube.ioi.service;

import com.wootube.ioi.service.dto.EmailCheckResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSignUpService {

    private final UserService userService;

    @Autowired
    public UserSignUpService(UserService userService) {
        this.userService = userService;
    }

    public EmailCheckResponseDto checkDuplicate(String email) {
        try {
            userService.findByEmail(email);
            return EmailCheckResponseDto.impossible();
        } catch (NotFoundUserException e) {
            return EmailCheckResponseDto.possible();
        }
    }
}
