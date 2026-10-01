package com.sogasari.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.response.CartItemResponse;
import com.sogasari.entity.User;
import com.sogasari.repository.UserRepository;
import com.sogasari.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public List<CartItemResponse> getCart(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return cartService.getCart(
                user.getId()
        );
    }

    @PostMapping("/{productId}")
    public CartItemResponse addToCart(
            @PathVariable Long productId,
            @RequestParam(
                    defaultValue = "1"
            ) Integer quantity,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return cartService.addToCart(
                user.getId(),
                productId,
                quantity
        );
    }

    @PutMapping("/{productId}")
    public CartItemResponse updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return cartService.updateQuantity(
                user.getId(),
                productId,
                quantity
        );
    }

    @DeleteMapping("/{productId}")
    public void removeFromCart(
            @PathVariable Long productId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        cartService.removeFromCart(
                user.getId(),
                productId
        );
    }

    @DeleteMapping
    public void clearCart(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        cartService.clearCart(
                user.getId()
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