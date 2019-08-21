package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.LoginFailedException;
import com.wootube.ioi.web.argument.Redirection;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class LoginFailedExceptionAdvice {

    @ExceptionHandler(LoginFailedException.class)
    public RedirectView loginFailedExceptionHandler(LoginFailedException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }
}
