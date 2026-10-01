package com.sogasari.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.response.CategoryResponse;
import com.sogasari.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAllCategories() {

        return categoryService.getAllCategories();
    }

    @GetMapping("/{slug}")
    public CategoryResponse getCategory(
            @PathVariable String slug
    ) {

        return categoryService.getCategoryBySlug(slug);
    }
}