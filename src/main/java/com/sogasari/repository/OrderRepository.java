package com.sogasari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

 import com.sogasari.entity.Order;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

 
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

}