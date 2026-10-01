package com.sogasari.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.CreateOrderRequest;
import com.sogasari.dto.response.OrderResponse;
import com.sogasari.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{orderNumber}")
    public OrderResponse getOrder(
            @PathVariable String orderNumber
    ) {
        return orderService.getOrderByOrderNumber(
                orderNumber
        );
    }


      @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders(
            Authentication authentication
    ) {

        String phone = authentication.getName();

        return orderService.getMyOrders(phone);
    }


    
}