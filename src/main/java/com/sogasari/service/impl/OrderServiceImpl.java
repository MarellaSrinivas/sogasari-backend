package com.sogasari.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.request.CreateOrderRequest;
import com.sogasari.dto.request.OrderItemRequest;
import com.sogasari.dto.response.OrderItemResponse;
import com.sogasari.dto.response.OrderResponse;
import com.sogasari.entity.Address;
import com.sogasari.entity.Order;
import com.sogasari.entity.OrderItem;
import com.sogasari.entity.OrderStatus;
import com.sogasari.entity.PaymentMethod;
import com.sogasari.entity.PaymentStatus;
import com.sogasari.entity.Product;
import com.sogasari.entity.ProductVariant;
import com.sogasari.entity.User;
import com.sogasari.repository.AddressRepository;
import com.sogasari.repository.OrderRepository;
import com.sogasari.repository.ProductRepository;
import com.sogasari.repository.UserRepository;
import com.sogasari.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal FREE_SHIPPING_LIMIT =
            BigDecimal.valueOf(999);

    private static final BigDecimal STANDARD_SHIPPING =
            BigDecimal.valueOf(99);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse createOrder(
            CreateOrderRequest request
    ) {

        // ==========================================
        // 1. FIND CUSTOMER
        // ==========================================

        User user =
                userRepository
                        .findByPhone(request.getPhone())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );


        // ==========================================
        // 2. VALIDATE ADDRESS
        // ==========================================

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                request.getAddressId(),
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Address not found"
                                )
                        );


        // ==========================================
        // 3. CREATE ORDER
        // ==========================================

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)

                // Address snapshot
                .shippingFullName(
                        address.getFullName()
                )
                .shippingPhone(
                        address.getPhone()
                )
                .shippingAddressLine1(
                        address.getAddressLine1()
                )
                .shippingAddressLine2(
                        address.getAddressLine2()
                )
                .shippingCity(
                        address.getCity()
                )
                .shippingState(
                        address.getState()
                )
                .shippingPincode(
                        address.getPincode()
                )

                        .paymentMethod(request.getPaymentMethod())

                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)

                .build();


        // ==========================================
        // 4. PROCESS ITEMS
        // ==========================================

        List<OrderItem> orderItems =
                new ArrayList<>();

        BigDecimal subtotal =
                BigDecimal.ZERO;


        for (
                OrderItemRequest itemRequest
                : request.getItems()
        ) {

            Product product =
                    productRepository
                            .findById(
                                    itemRequest.getProductId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found: "
                                                    + itemRequest.getProductId()
                                    )
                            );


            // ======================================
            // 5. PRODUCT ACTIVE CHECK
            // ======================================

            if (!Boolean.TRUE.equals(
                    product.getActive()
            )) {

                throw new RuntimeException(
                        "Product is no longer available: "
                                + product.getName()
                );
            }


            // ======================================
            // 6. QUANTITY CHECK
            // ======================================

            if (
                    itemRequest.getQuantity() == null
                            || itemRequest.getQuantity() < 1
            ) {

                throw new RuntimeException(
                        "Invalid quantity for product: "
                                + product.getName()
                );
            }


            int requestedQuantity =
                    itemRequest.getQuantity();


            // ======================================
            // 7. VARIANT CHECK
            // ======================================

            ProductVariant selectedVariant =
                    findVariant(
                            product,
                            itemRequest.getColor(),
                            itemRequest.getSize()
                    );


            // ======================================
            // 8. STOCK CHECK
            // ======================================

            if (selectedVariant != null) {

                if (!Boolean.TRUE.equals(
                        selectedVariant.getActive()
                )) {

                    throw new RuntimeException(
                            "Selected variant is unavailable: "
                                    + product.getName()
                    );
                }


                if (
                        selectedVariant.getStock()
                                < requestedQuantity
                ) {

                    throw new RuntimeException(
                            "Insufficient stock for "
                                    + product.getName()
                    );
                }

            } else {

                if (
                        product.getStock()
                                < requestedQuantity
                ) {

                    throw new RuntimeException(
                            "Insufficient stock for "
                                    + product.getName()
                    );
                }
            }


            // ======================================
            // 9. CALCULATE PRICE
            // ======================================

            BigDecimal itemPrice =
                    product.getPrice();


            // Variant additional price
            if (
                    selectedVariant != null
                            && selectedVariant
                            .getAdditionalPrice() != null
            ) {

                itemPrice =
                        itemPrice.add(
                                selectedVariant
                                        .getAdditionalPrice()
                        );
            }


            // ======================================
            // 10. LINE TOTAL
            // ======================================

            BigDecimal lineTotal =
                    itemPrice.multiply(
                            BigDecimal.valueOf(
                                    requestedQuantity
                            )
                    );


            // ======================================
            // 11. CREATE ORDER ITEM
            // ======================================

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(order)
                            .product(product)

                            // Snapshot
                            .productName(
                                    product.getName()
                            )
                            .sku(
                                    product.getSku()
                            )

                            .price(itemPrice)

                            .quantity(
                                    requestedQuantity
                            )

                            .color(
                                    itemRequest.getColor()
                            )

                            .size(
                                    itemRequest.getSize()
                            )

                            .lineTotal(lineTotal)

                            .build();


            orderItems.add(orderItem);


            // ======================================
            // 12. ADD TO SUBTOTAL
            // ======================================

            subtotal =
                    subtotal.add(lineTotal);
        }


        // ==========================================
        // 13. SHIPPING CALCULATION
        // ==========================================

        BigDecimal shipping;

        if (
                subtotal.compareTo(
                        FREE_SHIPPING_LIMIT
                ) >= 0
        ) {

            shipping = BigDecimal.ZERO;

        } else {

            shipping = STANDARD_SHIPPING;
        }


        // ==========================================
        // 14. TOTAL CALCULATION
        // ==========================================

        BigDecimal total =
                subtotal.add(shipping);


        // ==========================================
        // 15. SET ORDER TOTALS
        // ==========================================

        order.setSubtotal(subtotal);
        order.setShipping(shipping);
        order.setTotal(total);


        if (request.getPaymentMethod() == PaymentMethod.COD) {

    order.setOrderStatus(OrderStatus.CONFIRMED);
    order.setPaymentStatus(PaymentStatus.PENDING);
}


        // ==========================================
        // 16. ATTACH ITEMS
        // ==========================================

        orderItems.forEach(
                item -> item.setOrder(order)
        );

        order.setItems(orderItems);


        // ==========================================
        // 17. SAVE ORDER
        // ==========================================

        Order savedOrder =
                orderRepository.save(order);


        return mapToResponse(savedOrder);
    }


    // =================================================
    // FIND VARIANT
    // =================================================

    private ProductVariant findVariant(
            Product product,
            String color,
            String size
    ) {

        // No variant information supplied
        if (
                (color == null || color.isBlank())
                        && (size == null || size.isBlank())
        ) {

            return null;
        }


        return product.getVariants()
                .stream()
                .filter(
                        variant ->
                                Boolean.TRUE.equals(
                                        variant.getActive()
                                )
                )
                .filter(
                        variant ->
                                matches(
                                        variant.getColorName(),
                                        color
                                )
                )
                .filter(
                        variant ->
                                matches(
                                        variant.getSize(),
                                        size
                                )
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Selected product variant not found"
                        )
                );
    }


    private boolean matches(
            String databaseValue,
            String requestedValue
    ) {

        if (
                requestedValue == null
                        || requestedValue.isBlank()
        ) {

            return true;
        }

        return databaseValue != null
                && databaseValue.equalsIgnoreCase(
                        requestedValue.trim()
                );
    }


    // =================================================
    // ORDER NUMBER
    // =================================================

    private String generateOrderNumber() {

        String orderNumber;

        do {

            orderNumber =
                    "SGS-"
                            + System.currentTimeMillis()
                            + "-"
                            + UUID.randomUUID()
                            .toString()
                            .substring(0, 4)
                            .toUpperCase();

        } while (
                orderRepository.existsByOrderNumber(
                        orderNumber
                )
        );

        return orderNumber;
    }


    // =================================================
    // GET ORDER
    // =================================================

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(
            String orderNumber
    ) {

        Order order =
                orderRepository
                        .findByOrderNumber(orderNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                )
                        );

        return mapToResponse(order);
    }


    // =================================================
    // RESPONSE MAPPER
    // =================================================

    private OrderResponse mapToResponse(
            Order order
    ) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(
                                item ->
                                        OrderItemResponse
                                                .builder()
                                                .id(item.getId())
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

                .id(order.getId())

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

                .items(items)

                .createdAt(
                        order.getCreatedAt()
                )

                .build();
    }



    @Override
@Transactional(readOnly = true)
public List<OrderResponse> getMyOrders(String phone) {

    User user = userRepository
            .findByPhone(phone)
            .orElseThrow(() ->
                    new RuntimeException("Customer not found")
            );

    return orderRepository
            .findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream()
            .map(this::mapToResponse)
            .toList();
}
}