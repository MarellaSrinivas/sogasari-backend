package com.sogasari.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sogasari.dto.response.CategoryResponse;
import com.sogasari.entity.Category;
import com.sogasari.repository.CategoryRepository;
import com.sogasari.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository
                .findByParentIsNullAndActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryBySlug(
            String slug
    ) {

        Category category =
                categoryRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        return mapToResponse(category);
    }

    private CategoryResponse mapToResponse(
            Category category
    ) {

        List<CategoryResponse> children =
                category.getChildren()
                        .stream()
                        .filter(child ->
                                Boolean.TRUE.equals(
                                        child.getActive()
                                )
                        )
                        .map(this::mapToResponse)
                        .toList();

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(
                        category.getDescription()
                )
                .image(category.getImage())
                .active(category.getActive())
                .parentId(
                        category.getParent() != null
                                ? category.getParent().getId()
                                : null
                )
                .children(children)
                .build();
    }
}