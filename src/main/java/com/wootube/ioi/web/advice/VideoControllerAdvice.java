package com.wootube.ioi.web.advice;

import com.wootube.ioi.service.exception.*;
import com.wootube.ioi.web.argument.Redirection;
import com.wootube.ioi.web.controller.exception.InvalidUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class VideoControllerAdvice {
    @ExceptionHandler(UserAndWriterMisMatchException.class)
    public ResponseEntity<String> handleMismatchUserAndWriterException(UserAndWriterMisMatchException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserException.class)
    public RedirectView invalidUserExceptionHandler(InvalidUserException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/user/login");
    }

    @ExceptionHandler(NotFoundVideoIdException.class)
    public RedirectView notFoundVideoExceptionHandler(NotFoundVideoIdException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(NotMatchUserIdException.class)
    public RedirectView notMatchUserIdExceptionHandler(NotMatchUserIdException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler({FileUploadException.class, InvalidFileExtensionException.class})
    public RedirectView handleFileUploadException(RuntimeException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

    @ExceptionHandler(TitleMaxLenthException.class)
    public RedirectView handleTitleMaxLengthException(RedirectAttributes redirectAttributes, Redirection redirection, TitleMaxLenthException e) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

    @ExceptionHandler(DescriptionMaxLengthException.class)
    public RedirectView handleDescriptionLengthException(RedirectAttributes redirectAttributes, Redirection redirection, DescriptionMaxLengthException e) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }
}
