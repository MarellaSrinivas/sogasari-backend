package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.CartItem;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductIdAndColorAndSize(
            Long userId,
            Long productId,
            String color,
            String size
    );

    Optional<CartItem> findByUserIdAndProductId(
            Long userId,
            Long productId
    );

    void deleteByUserIdAndProductId(
            Long userId,
            Long productId
    );
}