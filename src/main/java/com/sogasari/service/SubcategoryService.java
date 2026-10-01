package com.sogasari.service;

import java.util.List;

import com.sogasari.dto.response.SubcategoryResponse;

public interface SubcategoryService {

    List<SubcategoryResponse>
    getByCategory(Long categoryId);

    SubcategoryResponse
    getBySlug(String slug);
}