package com.wootube.ioi.service;

import com.wootube.ioi.service.dto.EmailCheckResponseDto;
import com.wootube.ioi.service.exception.NotFoundUserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRestService {

    private UserService userService;

    @Autowired
    public UserRestService(UserService userService) {
        this.userService = userService;
    }

    public EmailCheckResponseDto checkDuplicate(String email) {
        try {
            userService.findByEmail(email);
            return new EmailCheckResponseDto("impossible");
        } catch (NotFoundUserException e) {
            return new EmailCheckResponseDto("possible");
        }
    }
}
