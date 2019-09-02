package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.VerifyKey;
import com.wootube.ioi.domain.repository.VerifyKeyRepository;
import com.wootube.ioi.service.exception.NotMatchVerifyKeyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class VerifyKeyServiceTest {

    @InjectMocks
    private VerifyKeyService verifyKeyService;

    @Mock
    private VerifyKeyRepository verifyKeyRepository;

    @DisplayName("verifyKey 확인 성공")
    @Test
    void confirmKey() {
        given(verifyKeyRepository.findByEmailAndVerifyKey("luffy@dev.com", "19930705")).willReturn(Optional.of(new VerifyKey("luffy@dev.com", "19930705")));

        assertTrue(verifyKeyService.confirmKey("luffy@dev.com", "19930705"));
    }

    @DisplayName("verifyKey 확인 실패")
    @Test
    void confirmKeyFailed() {
        given(verifyKeyRepository.findByEmailAndVerifyKey("luffy@dev.com", "19930705")).willReturn(Optional.of(new VerifyKey("luffy@dev.com", "19930705")));

        assertThrows(NotMatchVerifyKeyException.class, () -> verifyKeyService.confirmKey("bmo@dev.com", "19941102"));
    }
}