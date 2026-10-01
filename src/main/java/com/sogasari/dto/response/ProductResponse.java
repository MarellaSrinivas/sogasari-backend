package com.sogasari.dto.response;

import java.math.BigDecimal;
import java.util.List;

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
public class ProductResponse {

    private Long id;

    private String name;

    private String slug;

    private String sku;

    private Long categoryId;

    private String categoryName;

    private String categorySlug;

    private Long subcategoryId;

private String subcategoryName;

private String subcategorySlug;

    private String shortDescription;

    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer discount;

    private String badge;

    private Integer stock;

    private Boolean featured;

    private Boolean bestSeller;

    private Boolean newArrival;

    private Boolean active;

    private List<ProductImageResponse> images;

    private List<ProductVariantResponse> variants;
}