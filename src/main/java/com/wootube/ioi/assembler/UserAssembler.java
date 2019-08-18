package com.wootube.ioi.assembler;

import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.service.dto.SignUpRequestDto;

public class UserAssembler {

    public static User toDomain(SignUpRequestDto requestDto) {
        return new User(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
    }
}
