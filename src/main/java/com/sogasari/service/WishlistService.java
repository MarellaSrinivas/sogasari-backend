package com.sogasari.service;

import java.util.List;

import com.sogasari.dto.response.ProductResponse;

public interface WishlistService {

    List<ProductResponse> getWishlist(Long userId);

    ProductResponse addToWishlist(
            Long userId,
            Long productId
    );

    void removeFromWishlist(
            Long userId,
            Long productId
    );
}