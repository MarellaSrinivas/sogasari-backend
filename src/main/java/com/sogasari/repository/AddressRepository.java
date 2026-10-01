package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Address;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    List<Address> findByUserIdOrderByDefaultAddressDesc(
            Long userId
    );

    Optional<Address> findByIdAndUserId(
            Long id,
            Long userId
    );
}