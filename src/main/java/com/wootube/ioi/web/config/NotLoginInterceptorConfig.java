package com.wootube.ioi.web.config;

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
                .addPathPatterns("/user/mypage")
                .addPathPatterns("/user/logout");
    }
}
