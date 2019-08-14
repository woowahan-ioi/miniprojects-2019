package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.NotFoundUserException;
import com.wootube.ioi.web.session.SessionUser;

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

    public User login(LogInRequestDto logInRequestDto) {
        return userRepository.findByEmail(logInRequestDto.getEmail())
                .orElseThrow(NotFoundUserException::new)
                .matchPassword(logInRequestDto.getPassword());
    }

    public User update(SessionUser user, EditUserRequestDto editUserRequestDto) {
        return userRepository.findByEmail(user.getEmail())
                .orElseThrow(NotFoundUserException::new)
                .updateName(editUserRequestDto.getName());
    }
}
