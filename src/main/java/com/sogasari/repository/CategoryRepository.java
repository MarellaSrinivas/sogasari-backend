package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogasari.entity.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Category> findByParentIsNullAndActiveTrue();

    List<Category> findByParentIdAndActiveTrue(Long parentId);
}