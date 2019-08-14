package com.wootube.ioi.service;

import com.wootube.ioi.domain.User;
import com.wootube.ioi.repository.UserRepository;
import com.wootube.ioi.web.dto.SignUpRequestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(SignUpRequestDto signUpRequestDto) {
        return userRepository.save(signUpRequestDto.toUser());
    }
}
