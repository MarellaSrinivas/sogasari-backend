package com.sogasari.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.response.ProductResponse;
import com.sogasari.entity.Product;
import com.sogasari.entity.User;
import com.sogasari.entity.Wishlist;
import com.sogasari.repository.ProductRepository;
import com.sogasari.repository.UserRepository;
import com.sogasari.repository.WishlistRepository;
import com.sogasari.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl
        implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProductResponse> getWishlist(Long userId) {

        return wishlistRepository
                .findByUserId(userId)
                .stream()
                .map(wishlist -> mapToResponse(wishlist.getProduct()))
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse addToWishlist(
            Long userId,
            Long productId
    ) {

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

        boolean exists =
                wishlistRepository
                        .existsByUserIdAndProductId(
                                userId,
                                productId
                        );

        if (!exists) {

            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .product(product)
                    .build();

            wishlistRepository.save(wishlist);
        }

        return mapToResponse(product);
    }

    @Override
    @Transactional
    public void removeFromWishlist(
            Long userId,
            Long productId
    ) {

        wishlistRepository
                .deleteByUserIdAndProductId(
                        userId,
                        productId
                );
    }

    private ProductResponse mapToResponse(
            Product product
    ) {

        // Use the same mapping logic
        // you already have in ProductServiceImpl.

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .sku(product.getSku())
                .categoryId(
                        product.getCategory().getId()
                )
                .categoryName(
                        product.getCategory().getName()
                )
                .categorySlug(
                        product.getCategory().getSlug()
                )
                .shortDescription(
                        product.getShortDescription()
                )
                .description(
                        product.getDescription()
                )
                .price(product.getPrice())
                .originalPrice(
                        product.getOriginalPrice()
                )
                .discount(
                        product.getDiscount()
                )
                .badge(product.getBadge())
                .stock(product.getStock())
                .featured(product.getFeatured())
                .bestSeller(product.getBestSeller())
                .newArrival(product.getNewArrival())
                .active(product.getActive())
                .images(
                        product.getImages()
                                .stream()
                                .map(image ->
                                        com.sogasari.dto.response.ProductImageResponse
                                                .builder()
                                                .id(image.getId())
                                                .imageUrl(
                                                        image.getImageUrl()
                                                )
                                                .displayOrder(
                                                        image.getDisplayOrder()
                                                )
                                                .primaryImage(
                                                        image.getPrimaryImage()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .variants(
                        product.getVariants()
                                .stream()
                                .map(variant ->
                                        com.sogasari.dto.response.ProductVariantResponse
                                                .builder()
                                                .id(variant.getId())
                                                .colorName(
                                                        variant.getColorName()
                                                )
                                                .colorCode(
                                                        variant.getColorCode()
                                                )
                                                .size(
                                                        variant.getSize()
                                                )
                                                .stock(
                                                        variant.getStock()
                                                )
                                                .additionalPrice(
                                                        variant.getAdditionalPrice()
                                                )
                                                .active(
                                                        variant.getActive()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}