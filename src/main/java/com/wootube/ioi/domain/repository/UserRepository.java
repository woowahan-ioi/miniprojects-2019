package com.wootube.ioi.domain.repository;

import java.util.Optional;

import com.wootube.ioi.domain.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
