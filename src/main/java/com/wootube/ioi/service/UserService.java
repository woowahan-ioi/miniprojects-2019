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
import org.springframework.transaction.annotation.Transactional;

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
        return findByEmail(logInRequestDto.getEmail()).matchPassword(logInRequestDto.getPassword());
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public User update(SessionUser sessionUser, EditUserRequestDto editUserRequestDto) {
        return findByEmail(sessionUser.getEmail()).updateName(editUserRequestDto.getName());
    }

    public User delete(SessionUser sessionUser) {
        User deleteTargetUser = findByEmail(sessionUser.getEmail());
        userRepository.delete(deleteTargetUser);
        return deleteTargetUser;
    }
}
