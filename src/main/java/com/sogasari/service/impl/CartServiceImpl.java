package com.sogasari.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.response.CartItemResponse;
import com.sogasari.entity.CartItem;
import com.sogasari.entity.Product;
import com.sogasari.entity.User;
import com.sogasari.repository.CartItemRepository;
import com.sogasari.repository.ProductRepository;
import com.sogasari.repository.UserRepository;
import com.sogasari.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(Long userId) {

        return cartItemRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CartItemResponse addToCart(
            Long userId,
            Long productId,
            Integer quantity
    ) {

        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        )
                );

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new RuntimeException(
                    "Product is not available"
            );
        }

        CartItem cartItem =
                cartItemRepository
                        .findByUserIdAndProductId(
                                userId,
                                productId
                        )
                        .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + quantity
            );

        } else {

            cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        CartItem saved =
                cartItemRepository.save(cartItem);

        return mapToResponse(saved);
    }

    @Override
    public CartItemResponse updateQuantity(
            Long userId,
            Long productId,
            Integer quantity
    ) {

        if (quantity == null || quantity < 1) {
            throw new RuntimeException(
                    "Quantity must be at least 1"
            );
        }

        CartItem cartItem =
                cartItemRepository
                        .findByUserIdAndProductId(
                                userId,
                                productId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        cartItem.setQuantity(quantity);

        return mapToResponse(
                cartItemRepository.save(cartItem)
        );
    }

    @Override
    public void removeFromCart(
            Long userId,
            Long productId
    ) {

        cartItemRepository
                .deleteByUserIdAndProductId(
                        userId,
                        productId
                );
    }

    @Override
    public void clearCart(Long userId) {

        List<CartItem> items =
                cartItemRepository
                        .findByUserId(userId);

        cartItemRepository.deleteAll(items);
    }

    private CartItemResponse mapToResponse(
            CartItem item
    ) {

        Product product = item.getProduct();

        String image = "";

        if (
                product.getImages() != null &&
                !product.getImages().isEmpty()
        ) {

            var primaryImage =
                    product.getImages()
                            .stream()
                            .filter(imageItem ->
                                    Boolean.TRUE.equals(
                                            imageItem.getPrimaryImage()
                                    )
                            )
                            .findFirst()
                            .orElse(
                                    product.getImages().get(0)
                            );

            image = primaryImage.getImageUrl();
        }

        BigDecimal price =
                product.getPrice() != null
                        ? product.getPrice()
                        : BigDecimal.ZERO;

        BigDecimal lineTotal =
                price.multiply(
                        BigDecimal.valueOf(
                                item.getQuantity()
                        )
                );

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .category(
                        product.getCategory() != null
                                ? product.getCategory().getName()
                                : ""
                )
                .image(image)
                .price(price)
                .originalPrice(
                        product.getOriginalPrice()
                )
                .color(item.getColor())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .lineTotal(lineTotal)
                .build();
    }
}