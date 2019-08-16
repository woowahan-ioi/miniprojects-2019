package com.wootube.ioi.web.controller;

import com.wootube.ioi.service.UserRestService;
import com.wootube.ioi.service.dto.EmailCheckRequestDto;
import com.wootube.ioi.service.dto.EmailCheckResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserRestService userRestService;

    public UserRestController(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    @PostMapping("/users")
    public ResponseEntity<EmailCheckResponseDto> checkDuplicated(@RequestBody EmailCheckRequestDto requestDto) {
        return new ResponseEntity<>(userRestService.checkDuplicate(requestDto.getEmail()), HttpStatus.OK);
    }
}
