package com.wootube.ioi.service;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.LoginFailedException;
import com.wootube.ioi.service.exception.NotFoundUserException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
    public User updateUser(Long userId, EditUserRequestDto editUserRequestDto) {
        return findById(userId).updateName(editUserRequestDto.getName());
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
    }

    public User deleteUser(Long userId) {
        User deleteTargetUser = findById(userId);
        userRepository.delete(deleteTargetUser);
        return deleteTargetUser;
    }
}
