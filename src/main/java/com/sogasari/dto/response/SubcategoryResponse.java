package com.sogasari.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private String image;

    private Long categoryId;

    private String categoryName;

    private Boolean active;
}