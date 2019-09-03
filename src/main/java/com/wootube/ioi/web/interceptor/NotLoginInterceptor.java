package com.wootube.ioi.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@Component
public class NotLoginInterceptor extends HandlerInterceptorAdapter {
    private static final Pattern subscriptionPattern = Pattern.compile("^((/api/subscriptions/)[0-9]+)$");
    private static final Pattern videoPattern = Pattern.compile("^((/videos/)[0-9]+)$");
    private static final Pattern videoAPIPattern = Pattern.compile("^(/api/videos/)[a-zA-Z]+$");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String servletPath = request.getServletPath();
        if ((subscriptionPattern.matcher(servletPath).matches()
                || videoPattern.matcher(servletPath).matches())
                || videoAPIPattern.matcher(servletPath).matches()
                && request.getMethod().equals("GET")) {
            return true;
        }
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        }
        return true;
    }
}
