package com.sogasari.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.response.ProductResponse;
import com.sogasari.entity.User;
import com.sogasari.repository.UserRepository;
import com.sogasari.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @GetMapping
    public List<ProductResponse> getWishlist(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return wishlistService.getWishlist(
                user.getId()
        );
    }

    @PostMapping("/{productId}")
    public ProductResponse addToWishlist(
            @PathVariable Long productId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return wishlistService.addToWishlist(
                user.getId(),
                productId
        );
    }

    @DeleteMapping("/{productId}")
    public void removeFromWishlist(
            @PathVariable Long productId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        wishlistService.removeFromWishlist(
                user.getId(),
                productId
        );
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        String phone = authentication.getName();

        return userRepository
                .findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}