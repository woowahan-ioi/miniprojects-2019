package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndActiveTrue(Long id);
}
