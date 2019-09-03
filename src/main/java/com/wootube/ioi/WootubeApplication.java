package com.wootube.ioi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

@SpringBootApplication
@PropertySource("classpath:email.properties")
public class WootubeApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WootubeApplication.class, args);
    }
}
