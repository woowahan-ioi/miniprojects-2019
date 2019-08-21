package com.wootube.ioi.web.advice;

import com.wootube.ioi.domain.exception.NotMatchCommentException;
import com.wootube.ioi.domain.exception.NotMatchVideoException;
import com.wootube.ioi.domain.exception.NotMatchWriterException;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import com.wootube.ioi.service.exception.NotFoundReplyException;

import com.wootube.ioi.service.exception.NotFoundVideoException;
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
    public ResponseEntity<String> handleNotFoundVideoException(NotFoundVideoException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchCommentException(NotMatchCommentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchVideoException(NotMatchVideoException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchWriterException(NotMatchWriterException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
