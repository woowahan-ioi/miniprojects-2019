package com.wootube.ioi.web.advice;

import com.wootube.ioi.domain.exception.NotMatchCommentException;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import com.wootube.ioi.service.exception.NotFoundReplyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundCommentException(NotFoundCommentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundReplyException(NotFoundReplyException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundReplyException(NotMatchCommentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
