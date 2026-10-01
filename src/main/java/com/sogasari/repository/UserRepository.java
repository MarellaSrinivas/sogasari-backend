package com.sogasari.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);
}