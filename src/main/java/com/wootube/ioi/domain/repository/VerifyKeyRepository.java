package com.wootube.ioi.domain.repository;

import java.util.Optional;

import com.wootube.ioi.domain.model.VerifyKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyKeyRepository extends JpaRepository<VerifyKey, Long> {
	Optional<VerifyKey> findByEmailAndVerifyKey(String email, String verifyKey);
}
