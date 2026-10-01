package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Subcategory;

public interface SubcategoryRepository
        extends JpaRepository<Subcategory, Long> {

    List<Subcategory> findByCategoryIdAndActiveTrue(
            Long categoryId
    );

    Optional<Subcategory> findBySlug(
            String slug
    );

    boolean existsBySlug(
            String slug
    );
}