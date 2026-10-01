package com.sogasari.service;

import com.sogasari.dto.request.CreatePaymentRequest;
import com.sogasari.dto.request.VerifyPaymentRequest;
import com.sogasari.dto.response.OrderResponse;
import com.sogasari.dto.response.PaymentOrderResponse;

public interface PaymentService {

    PaymentOrderResponse createPaymentOrder(
            CreatePaymentRequest request
    );

    OrderResponse verifyPayment(
            VerifyPaymentRequest request
    );
}