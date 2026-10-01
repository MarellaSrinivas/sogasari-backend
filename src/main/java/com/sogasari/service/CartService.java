package com.sogasari.service;

import java.util.List;

import com.sogasari.dto.response.CartItemResponse;

public interface CartService {

    List<CartItemResponse> getCart(Long userId);

    CartItemResponse addToCart(
            Long userId,
            Long productId,
            Integer quantity
    );

    CartItemResponse updateQuantity(
            Long userId,
            Long productId,
            Integer quantity
    );

    void removeFromCart(
            Long userId,
            Long productId
    );

    void clearCart(Long userId);
}