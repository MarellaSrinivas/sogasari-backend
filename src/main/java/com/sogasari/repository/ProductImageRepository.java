package com.sogasari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.ProductImage;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(
            Long productId
    );

    int countByProductId(
            Long productId
    );
}