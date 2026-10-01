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
public class PaymentOrderResponse {

    private String orderNumber;

    private String razorpayOrderId;

    private String keyId;

    private Long amount;

    private String currency;
}