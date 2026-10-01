package com.sogasari.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sogasari.dto.response.SubcategoryResponse;
import com.sogasari.entity.Subcategory;
import com.sogasari.repository.SubcategoryRepository;
import com.sogasari.service.SubcategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl
        implements SubcategoryService {

    private final SubcategoryRepository
            subcategoryRepository;


    @Override
    public List<SubcategoryResponse>
    getByCategory(Long categoryId) {

        return subcategoryRepository
                .findByCategoryIdAndActiveTrue(
                        categoryId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public SubcategoryResponse
    getBySlug(String slug) {

        Subcategory subcategory =
                subcategoryRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new RuntimeException(
                                    "Subcategory not found"
                                )
                        );

        return mapToResponse(
                subcategory
        );
    }


    private SubcategoryResponse
    mapToResponse(
            Subcategory subcategory
    ) {

        return SubcategoryResponse.builder()

                .id(subcategory.getId())

                .name(subcategory.getName())

                .slug(subcategory.getSlug())

                .description(
                        subcategory.getDescription()
                )

                .image(
                        subcategory.getImage()
                )

                .categoryId(
                        subcategory
                                .getCategory()
                                .getId()
                )

                .categoryName(
                        subcategory
                                .getCategory()
                                .getName()
                )

                .active(
                        subcategory.getActive()
                )

                .build();
    }
}