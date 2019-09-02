package com.wootube.ioi.web.advice;

import com.wootube.ioi.domain.exception.ActivatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class ActivatedExceptionAdvice {

    @ExceptionHandler(ActivatedException.class)
    public RedirectView activatedExceptionExceptionHandler(ActivatedException e) {
        log.debug(e.getMessage());
        return new RedirectView("/errors/unknown");
    }
}
