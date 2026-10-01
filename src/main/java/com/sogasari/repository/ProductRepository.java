package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(String sku);

    List<Product> findByActiveTrue();

    List<Product> findByActiveTrueAndNewArrivalTrue();

    List<Product> findByActiveTrueAndBestSellerTrue();

    List<Product> findByActiveTrueAndFeaturedTrue();

    List<Product> findByCategorySlugAndActiveTrue(
            String slug
    );

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(
            String name
    );

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);
}