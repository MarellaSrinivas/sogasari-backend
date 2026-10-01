package com.sogasari.service;

import java.util.List;

import com.sogasari.dto.response.CategoryResponse;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryBySlug(String slug);
}