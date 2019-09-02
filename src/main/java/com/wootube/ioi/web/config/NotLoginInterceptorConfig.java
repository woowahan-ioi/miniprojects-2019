package com.wootube.ioi.web.config;

import java.util.Arrays;

import com.wootube.ioi.web.interceptor.NotLoginInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class NotLoginInterceptorConfig implements WebMvcConfigurer {
    @Qualifier(value = "notLoginInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public NotLoginInterceptorConfig(NotLoginInterceptor notLoginInterceptor) {
        this.handlerInterceptor = notLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/videos/**")
                .addPathPatterns("/api/videos/**")
                .addPathPatterns("/api/videos/**/comments/**")
                .addPathPatterns("/api/subscriptions/**")
                .addPathPatterns("/api/videos/**/comments/**/replies/**")
                .excludePathPatterns(Arrays.asList(
                        "/api/videos/**/comments/sort/updatetime",
                        "/api/videos/**/comments/**/replies/sort/updatetime",
                        "/api/videos/**/likes/counts",
                        "/user/signup",
                        "/user/login"
                ));
    }
}
