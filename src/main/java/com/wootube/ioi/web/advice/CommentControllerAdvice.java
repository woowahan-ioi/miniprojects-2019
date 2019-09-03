package com.wootube.ioi.web.advice;

import com.wootube.ioi.domain.exception.NotMatchCommentException;
import com.wootube.ioi.domain.exception.NotMatchVideoException;
import com.wootube.ioi.domain.exception.NotMatchWriterException;
import com.wootube.ioi.service.exception.NotFoundCommentException;
import com.wootube.ioi.service.exception.NotFoundReplyException;
import com.wootube.ioi.service.exception.NotFoundVideoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommentControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundCommentException(NotFoundCommentException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundReplyException(NotFoundReplyException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundVideoException(NotFoundVideoException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchCommentException(NotMatchCommentException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchVideoException(NotMatchVideoException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotMatchWriterException(NotMatchWriterException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
