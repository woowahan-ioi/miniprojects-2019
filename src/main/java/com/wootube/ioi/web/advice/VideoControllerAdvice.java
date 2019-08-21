package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.UserAndWriterMisMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VideoControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handleMismatchUserAndWriterException(UserAndWriterMisMatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
