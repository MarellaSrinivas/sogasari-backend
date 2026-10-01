package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Wishlist;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndProductId(
            Long userId,
            Long productId
    );

    boolean existsByUserIdAndProductId(
            Long userId,
            Long productId
    );

    void deleteByUserIdAndProductId(
            Long userId,
            Long productId
    );
}