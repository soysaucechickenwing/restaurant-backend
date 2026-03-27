package com.johngoodtime.demo.repository;

import com.johngoodtime.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);
    Optional<Order>findByStripePaymentId(String stripePaymentId);
}
