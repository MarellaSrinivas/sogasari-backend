package com.sogasari.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private String image;

    private Boolean active;

    private Long parentId;

    private List<CategoryResponse> children;
}