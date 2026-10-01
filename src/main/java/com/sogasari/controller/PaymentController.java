package com.sogasari.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.CreatePaymentRequest;
import com.sogasari.dto.request.VerifyPaymentRequest;
import com.sogasari.dto.response.OrderResponse;
import com.sogasari.dto.response.PaymentOrderResponse;
import com.sogasari.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public PaymentOrderResponse createPayment(
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        return paymentService.createPaymentOrder(
                request
        );
    }

    @PostMapping("/verify")
    public OrderResponse verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request
    ) {
        return paymentService.verifyPayment(
                request
        );
    }
}