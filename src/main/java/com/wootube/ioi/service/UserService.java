package com.wootube.ioi.service;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.LoginFailedException;
import com.wootube.ioi.service.exception.NotFoundUserException;
import com.wootube.ioi.web.session.UserSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User createUser(SignUpRequestDto signUpRequestDto) {
        return userRepository.save(modelMapper.map(signUpRequestDto, User.class));
    }

    public User readUser(LogInRequestDto logInRequestDto) {
        try {
            return findByEmail(logInRequestDto.getEmail()).matchPassword(logInRequestDto.getPassword());
        } catch (NotFoundUserException | NotMatchPasswordException e) {
            throw new LoginFailedException();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public User updateUser(UserSession userSession, EditUserRequestDto editUserRequestDto) {
        return findByEmail(userSession.getEmail()).updateName(editUserRequestDto.getName());
    }

    public User deleteUser(UserSession userSession) {
        User deleteTargetUser = findByEmail(userSession.getEmail());
        userRepository.delete(deleteTargetUser);
        return deleteTargetUser;
    }
}
