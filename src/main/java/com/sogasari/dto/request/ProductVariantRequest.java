package com.sogasari.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantRequest {

    private String colorName;

    private String colorCode;

    private String size;

    @Min(0)
    private Integer stock = 0;

    private BigDecimal additionalPrice;

    private Boolean active = true;
}