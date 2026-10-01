package com.sogasari.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String name;

    private String slug;

    private String category;

    private String image;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String color;

    private String size;

    private Integer quantity;

    private BigDecimal lineTotal;
}