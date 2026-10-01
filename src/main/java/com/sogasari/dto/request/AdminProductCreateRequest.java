package com.sogasari.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductCreateRequest {

    // =========================
    // BASIC INFORMATION
    // =========================

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    @NotBlank
    private String sku;


    // =========================
    // CATEGORY
    // =========================

    @NotNull
    private Long categoryId;


    // =========================
    // DESCRIPTION
    // =========================

    private String shortDescription;

    private String description;


    // =========================
    // PRICE
    // =========================

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @DecimalMin("0.0")
    private BigDecimal originalPrice;

    private Integer discount;


    // =========================
    // PRODUCT CARD
    // =========================

    private String badge;


    // =========================
    // INVENTORY
    // =========================

    @NotNull
    @Min(0)
    private Integer stock;


    // =========================
    // FLAGS
    // =========================

    private Boolean featured = false;

    private Boolean bestSeller = false;

    private Boolean newArrival = false;

    private Boolean active = true;


    // =========================
    // VARIANTS
    // =========================

    @Valid
    private List<ProductVariantRequest> variants;

 
 }