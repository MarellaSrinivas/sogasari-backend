package com.sogasari.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.response.SubcategoryResponse;
import com.sogasari.service.SubcategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService
            subcategoryService;


    @GetMapping("/category/{categoryId}")
    public List<SubcategoryResponse>
    getByCategory(
            @PathVariable Long categoryId
    ) {

        return subcategoryService
                .getByCategory(categoryId);
    }


    @GetMapping("/{slug}")
    public SubcategoryResponse
    getBySlug(
            @PathVariable String slug
    ) {

        return subcategoryService
                .getBySlug(slug);
    }
}