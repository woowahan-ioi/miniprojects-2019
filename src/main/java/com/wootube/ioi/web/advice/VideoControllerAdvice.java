package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.UserAndWriterMisMatchException;
import com.wootube.ioi.web.controller.exception.InvalidUserException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class VideoControllerAdvice {

    @ExceptionHandler(UserAndWriterMisMatchException.class)
    public ResponseEntity<String> handleMismatchUserAndWriterException(UserAndWriterMisMatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserException.class)
    public RedirectView invalidUserException(InvalidUserException e) {
        return new RedirectView("/user/login");
    }
}
