package com.sogasari.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.sogasari.config.RazorpayProperties;
import com.sogasari.dto.request.CreatePaymentRequest;
import com.sogasari.dto.request.VerifyPaymentRequest;
import com.sogasari.dto.response.OrderItemResponse;
import com.sogasari.dto.response.OrderResponse;
import com.sogasari.dto.response.PaymentOrderResponse;
import com.sogasari.entity.Order;
import com.sogasari.entity.OrderStatus;
import com.sogasari.entity.PaymentStatus;
import com.sogasari.repository.OrderRepository;
import com.sogasari.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl
        implements PaymentService {

    private final OrderRepository orderRepository;

    private final RazorpayClient razorpayClient;

    private final RazorpayProperties razorpayProperties;


    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    @Override
    public PaymentOrderResponse createPaymentOrder(
            CreatePaymentRequest request
    ) {

        Order order =
                orderRepository
                        .findByOrderNumber(
                                request.getOrderNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                )
                        );


        // Already paid
        if (order.getPaymentStatus()
                == PaymentStatus.PAID) {

            throw new RuntimeException(
                    "Order has already been paid"
            );
        }


        // COD should never come here
        if (order.getPaymentMethod() != null
                && order.getPaymentMethod().name()
                .equals("COD")) {

            throw new RuntimeException(
                    "COD orders do not require online payment"
            );
        }


        // Order must still be pending
        if (order.getOrderStatus()
                == OrderStatus.CONFIRMED) {

            throw new RuntimeException(
                    "Order is already confirmed"
            );
        }


        BigDecimal total =
                order.getTotal();


        // Convert INR to paise
        long amountInPaise =
                total
                        .multiply(
                                BigDecimal.valueOf(100)
                        )
                        .longValueExact();


        try {

            JSONObject options =
                    new JSONObject();


            options.put(
                    "amount",
                    amountInPaise
            );


            options.put(
                    "currency",
                    "INR"
            );


            options.put(
                    "receipt",
                    order.getOrderNumber()
            );


            // =================================================
            // CREATE RAZORPAY ORDER
            // =================================================

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(
                            options
                    );


            // Razorpay order ID
            String razorpayOrderId =
                    razorpayOrder.get("id");


            // =================================================
            // SAVE RAZORPAY ORDER ID
            // =================================================

            order.setRazorpayOrderId(
                    razorpayOrderId
            );


            orderRepository.save(order);


            // =================================================
            // SEND DATA TO REACT
            // =================================================

            return PaymentOrderResponse.builder()

                    .orderNumber(
                            order.getOrderNumber()
                    )

                    .razorpayOrderId(
                            razorpayOrderId
                    )

                    .keyId(
                            razorpayProperties
                                    .getKeyId()
                    )

                    .amount(
                            amountInPaise
                    )

                    .currency(
                            "INR"
                    )

                    .build();


        } catch (RazorpayException exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Unable to create payment order"
            );
        }
    }


    // =========================================================
    // VERIFY PAYMENT
    // =========================================================

    @Override
    public OrderResponse verifyPayment(
            VerifyPaymentRequest request
    ) {

        Order order =
                orderRepository
                        .findByOrderNumber(
                                request.getOrderNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                )
                        );


        // Already paid
        if (order.getPaymentStatus()
                == PaymentStatus.PAID) {

            return mapToResponse(order);
        }


        // =====================================================
        // VERIFY RAZORPAY ORDER ID
        // =====================================================

        if (order.getRazorpayOrderId() == null
                || !request
                        .getRazorpayOrderId()
                        .equals(
                                order.getRazorpayOrderId()
                        )) {

            throw new RuntimeException(
                    "Razorpay order does not match"
            );
        }


        try {

            JSONObject attributes =
                    new JSONObject();


            attributes.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );


            attributes.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );


            attributes.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );


            // =================================================
            // VERIFY RAZORPAY SIGNATURE
            // =================================================

            Utils.verifyPaymentSignature(
                    attributes,
                    razorpayProperties
                            .getKeySecret()
            );


            // =================================================
            // PAYMENT SUCCESS
            // =================================================

            order.setPaymentStatus(
                    PaymentStatus.PAID
            );


            order.setOrderStatus(
                    OrderStatus.CONFIRMED
            );


            order.setPaymentId(
                    request.getRazorpayPaymentId()
            );


            Order savedOrder =
                    orderRepository.save(order);


            return mapToResponse(
                    savedOrder
            );


        } catch (RazorpayException exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Payment verification failed"
            );
        }
    }


    // =========================================================
    // MAP ORDER RESPONSE
    // =========================================================

    private OrderResponse mapToResponse(
            Order order
    ) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item ->
                                OrderItemResponse
                                        .builder()

                                        .id(
                                                item.getId()
                                        )

                                        .productId(
                                                item.getProduct()
                                                        .getId()
                                        )

                                        .productName(
                                                item.getProductName()
                                        )

                                        .sku(
                                                item.getSku()
                                        )

                                        .price(
                                                item.getPrice()
                                        )

                                        .quantity(
                                                item.getQuantity()
                                        )

                                        .color(
                                                item.getColor()
                                        )

                                        .size(
                                                item.getSize()
                                        )

                                        .lineTotal(
                                                item.getLineTotal()
                                        )

                                        .build()
                        )
                        .toList();


        return OrderResponse.builder()

                .id(
                        order.getId()
                )

                .orderNumber(
                        order.getOrderNumber()
                )

                .subtotal(
                        order.getSubtotal()
                )

                .shipping(
                        order.getShipping()
                )

                .total(
                        order.getTotal()
                )

                .orderStatus(
                        order.getOrderStatus()
                                .name()
                )

                .paymentStatus(
                        order.getPaymentStatus()
                                .name()
                )

                .paymentMethod(
                        order.getPaymentMethod()
                                .name()
                )

                .paymentId(
                        order.getPaymentId()
                )

                .shippingFullName(
                        order.getShippingFullName()
                )

                .shippingPhone(
                        order.getShippingPhone()
                )

                .shippingAddressLine1(
                        order.getShippingAddressLine1()
                )

                .shippingAddressLine2(
                        order.getShippingAddressLine2()
                )

                .shippingCity(
                        order.getShippingCity()
                )

                .shippingState(
                        order.getShippingState()
                )

                .shippingPincode(
                        order.getShippingPincode()
                )

                .items(
                        items
                )

                .createdAt(
                        order.getCreatedAt()
                )

                .build();
    }
}