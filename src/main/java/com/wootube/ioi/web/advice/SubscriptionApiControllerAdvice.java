package com.wootube.ioi.web.advice;

import com.wootube.ioi.domain.exception.IllegalSubscriptionException;
import com.wootube.ioi.service.exception.AlreadySubscribedException;
import com.wootube.ioi.service.exception.IllegalUnsubscriptionException;
import com.wootube.ioi.service.exception.NotFoundSubscriptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class SubscriptionApiControllerAdvice {

    @ExceptionHandler(AlreadySubscribedException.class)
    public ResponseEntity<String> handleAlreadySubscribedException(AlreadySubscribedException e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalSubscriptionException.class)
    public ResponseEntity<String> handleIllegalSubscriptionException(IllegalSubscriptionException e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalUnsubscriptionException.class)
    public ResponseEntity<String> handleIllegalUnsubscriptionException(IllegalUnsubscriptionException e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundSubscriptionException.class)
    public ResponseEntity<String> handleNotFoundSubscriptionException(NotFoundSubscriptionException e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
