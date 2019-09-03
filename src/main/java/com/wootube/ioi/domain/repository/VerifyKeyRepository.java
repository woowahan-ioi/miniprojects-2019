package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.VerifyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyKeyRepository extends JpaRepository<VerifyKey, Long> {
    Optional<VerifyKey> findByEmailAndVerifyKey(String email, String verifyKey);
}
