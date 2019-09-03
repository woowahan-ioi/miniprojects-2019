package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.SendFailException;
import com.wootube.ioi.web.argument.Redirection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class SendFailExceptionAdvice {

    @ExceptionHandler(SendFailException.class)
    public RedirectView sendFailExceptionHandler(SendFailException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }
}
