package com.wootube.ioi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WootubeApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml,"
            + "classpath:application.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(WootubeApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
