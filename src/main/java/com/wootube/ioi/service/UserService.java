package com.wootube.ioi.service;

import com.wootube.ioi.domain.exception.NotMatchPasswordException;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.UserRepository;
import com.wootube.ioi.service.dto.EditUserRequestDto;
import com.wootube.ioi.service.dto.LogInRequestDto;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.InActivatedUserException;
import com.wootube.ioi.service.exception.LoginFailedException;
import com.wootube.ioi.service.exception.NotFoundUserException;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final EmailService emailService;
	private final VerifyKeyService verifyKeyService;

	public UserService(UserRepository userRepository, ModelMapper modelMapper, EmailService emailService, VerifyKeyService verifyKeyService) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.emailService = emailService;
		this.verifyKeyService = verifyKeyService;
	}

	public User createUser(SignUpRequestDto signUpRequestDto) {
		return userRepository.save(modelMapper.map(signUpRequestDto, User.class));
	}

	public User readUser(LogInRequestDto logInRequestDto) {
		try {
			User savedEmail = findByEmail(logInRequestDto.getEmail());
			checkInActive(savedEmail);
			return savedEmail.matchPassword(logInRequestDto.getPassword());
		} catch (NotFoundUserException | NotMatchPasswordException e) {
			throw new LoginFailedException();
		}
	}

	private void checkInActive(User savedEmail) {
		if (!savedEmail.isActive()) {
			emailService.sendMessage(savedEmail.getEmail());
			throw new InActivatedUserException();
		}
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(NotFoundUserException::new);
	}

	@Transactional
	public User updateUser(Long userId, EditUserRequestDto editUserRequestDto) {
		return findByIdAndIsActiveTrue(userId).updateName(editUserRequestDto.getName());
	}

	public User findByIdAndIsActiveTrue(Long userId) {
		return userRepository.findByIdAndActiveTrue(userId)
				.orElseThrow(NotFoundUserException::new);
	}

	@Transactional
	public User deleteUser(Long userId) {
		User deleteTargetUser = findByIdAndIsActiveTrue(userId);
		deleteTargetUser.softDelete();
		return deleteTargetUser;
	}

	@Transactional
	public void activateUser(String email, String verifyKey) {
		if (verifyKeyService.confirmKey(email, verifyKey)) {
			findByEmail(email).activateUser();
		}
	}
}
