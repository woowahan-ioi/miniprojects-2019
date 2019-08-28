package com.wootube.ioi;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WootubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(WootubeApplication.class, args);
    }

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner commendLineRunner1() {
        return args ->
            userRepository.save(new User("tiber", "tiber@naver.com", "1234qwer"));
    }
}
