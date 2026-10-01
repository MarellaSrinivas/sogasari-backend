package com.sogasari.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class OrderResponse {

    private Long id;

    private String orderNumber;

    private BigDecimal subtotal;

    private BigDecimal shipping;

    private BigDecimal total;

    private String orderStatus;

    private String paymentStatus;

    private String paymentId;

    private String shippingFullName;

    private String shippingPhone;

    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingCity;

    private String shippingState;

    private String shippingPincode;

    private List<OrderItemResponse> items;

    private LocalDateTime createdAt;

    private String paymentMethod;
}