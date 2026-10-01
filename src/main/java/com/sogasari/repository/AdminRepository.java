package com.sogasari.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Admin;

public interface AdminRepository
        extends JpaRepository<Admin, Long> {

    Optional<Admin> findByPhoneAndActiveTrue(
            String phone
    );
}