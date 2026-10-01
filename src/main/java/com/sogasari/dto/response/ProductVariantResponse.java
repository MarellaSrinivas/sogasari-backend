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
public class ProductVariantResponse {

    private Long id;

    private String colorName;

    private String colorCode;

    private String size;

    private Integer stock;

    private BigDecimal additionalPrice;

    private Boolean active;
}