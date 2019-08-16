package com.wootube.ioi.service;

import com.wootube.ioi.service.dto.EmailCheckResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRestService {

    private static final EmailCheckResponseDto NOT_DUPLICATED_EMAIL_RESPONSE_DTO = new EmailCheckResponseDto("possible");
    private static final EmailCheckResponseDto DUPLICATED_EMAIL_RESPONSE_DTO = new EmailCheckResponseDto("impossible");

    private UserService userService;

    @Autowired
    public UserRestService(UserService userService) {
        this.userService = userService;
    }

    public EmailCheckResponseDto checkDuplicate(String email) {
        try {
            userService.findByEmail(email);
            return DUPLICATED_EMAIL_RESPONSE_DTO;
        } catch (NotFoundUserException e) {
            return NOT_DUPLICATED_EMAIL_RESPONSE_DTO;
        }
    }
}
