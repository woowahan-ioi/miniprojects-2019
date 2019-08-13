package com.wootube.ioi.repository;

import com.wootube.ioi.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
