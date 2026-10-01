package com.sogasari.service;
import java.util.List;

import com.sogasari.dto.request.CreateOrderRequest;
import com.sogasari.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(
            CreateOrderRequest request
    );

    OrderResponse getOrderByOrderNumber(
            String orderNumber
    );


    List<OrderResponse> getMyOrders(
            String phone
    );
}