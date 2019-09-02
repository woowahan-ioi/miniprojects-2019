package com.wootube.ioi.web.config;

import com.wootube.ioi.web.interceptor.NotLoginCommentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class NotLoginInterceptorCommentConfig implements WebMvcConfigurer {
    @Qualifier(value = "notLoginCommentInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Autowired
    public NotLoginInterceptorCommentConfig(NotLoginCommentInterceptor notLoginCommentInterceptor) {
        this.handlerInterceptor = notLoginCommentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/api/videos/**/comments/**")
                .addPathPatterns("/api/videos/**/comments")
                .addPathPatterns("/api/videos/**/comments/**/replies/**")
                .addPathPatterns("/api/videos/**/comments/**/replies")
                .excludePathPatterns(Arrays.asList(
                        "/api/videos/**/comments/sort/updatetime",
                        "/api/videos/**/comments/sort/likecount",
                        "/api/videos/**/comments/**/replies/sort/updatetime"
                ));
    }
}
