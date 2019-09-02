package com.wootube.ioi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/errors")
@Controller
public class ErrorController {
    @GetMapping("/unknown")
    public RedirectView error(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errors", "뭔가 알 수 없는 에러가 발생했습니다.");
        return new RedirectView("/user/login");
    }
}
