package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.UserSignUpService;
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
public class UserApiController {

    private final UserSignUpService userSignUpService;

    public UserApiController(UserSignUpService userSignUpService) {
        this.userSignUpService = userSignUpService;
    }

    @PostMapping("/users")
    public ResponseEntity<EmailCheckResponseDto> checkDuplicated(@RequestBody EmailCheckRequestDto requestDto) {
        return new ResponseEntity<>(userSignUpService.checkDuplicate(requestDto.getEmail()), HttpStatus.OK);
    }
}
