package com.wootube.ioi.domain.repository;

import com.wootube.ioi.assembler.UserAssembler;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.SignUpRequestDto;
import com.wootube.ioi.service.exception.NotFoundUserException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 데이터 찾기")
    @Test
    void findByEmail() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("루피", "luffy@luffy.com", "1234567a");
        userRepository.save(UserAssembler.toDomain(signUpRequestDto));

        User foundUser = userRepository.findByEmail(signUpRequestDto.getEmail())
                .orElseThrow(NotFoundUserException::new);

        assertThat(foundUser.getName()).isEqualTo(signUpRequestDto.getName());
        assertThat(foundUser.getEmail()).isEqualTo(signUpRequestDto.getEmail());
        assertThat(foundUser.getPassword()).isEqualTo(signUpRequestDto.getPassword());
    }
}