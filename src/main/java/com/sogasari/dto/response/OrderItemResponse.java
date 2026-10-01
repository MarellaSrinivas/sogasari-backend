package com.sogasari.dto.response;

import java.math.BigDecimal;

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
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String sku;

    private BigDecimal price;

    private Integer quantity;

    private String color;

    private String size;

    private BigDecimal lineTotal;
}