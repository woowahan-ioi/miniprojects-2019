package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.VerifyKey;
import com.wootube.ioi.domain.model.VerifyKeyFactory;
import com.wootube.ioi.domain.repository.VerifyKeyRepository;
import com.wootube.ioi.service.exception.NotMatchVerifyKeyException;
import org.springframework.stereotype.Service;

@Service
public class VerifyKeyService {
    private VerifyKeyRepository verifyKeyRepository;

    public VerifyKeyService(VerifyKeyRepository verifyKeyRepository) {
        this.verifyKeyRepository = verifyKeyRepository;
    }

    public boolean confirmKey(String email, String verifyKey) {
        VerifyKey savedVerifyKey = verifyKeyRepository.findByEmailAndVerifyKey(email, verifyKey)
                .orElseThrow(NotMatchVerifyKeyException::new);
        verifyKeyRepository.delete(savedVerifyKey);
        return true;
    }

    public String createVerifyKey(String inActiveUserEmail) {
        String key = VerifyKeyFactory.createKey();
        VerifyKey verifyKey = new VerifyKey(inActiveUserEmail, key);
        verifyKeyRepository.save(verifyKey);
        return key;
    }
}
