package com.wootube.ioi.web.config;

import java.util.List;

import com.wootube.ioi.web.argument.RedirectionArgumentResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RedirectionArgumentResolver());
    }
}
