package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.NotMatchVerifyKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class NotMatchVerifyKeyExceptionAdvice {

    @ExceptionHandler(NotMatchVerifyKeyException.class)
    public RedirectView inActivatedUserExceptionHandler(NotMatchVerifyKeyException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/user/login");
    }
}
