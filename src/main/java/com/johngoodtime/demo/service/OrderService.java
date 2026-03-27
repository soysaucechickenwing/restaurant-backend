package com.johngoodtime.demo.service;

import com.johngoodtime.demo.model.MenuItem;
import com.johngoodtime.demo.model.Order;
import com.johngoodtime.demo.model.OrderItem;
import com.johngoodtime.demo.repository.MenuItemRepository;
import com.johngoodtime.demo.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrderService {
    @Value("${app.order.preparation-minutes}")
    private int preparationMinutes;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        // 计算金额
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + item.getMenuItemId()));
            item.setMenuItemName(menuItem.getName());
            item.setUnitPrice(menuItem.getPrice());
            item.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrder(order);
            subtotal = subtotal.add(item.getSubtotal());
        }
        // MA税率6.25%
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.0625")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax);

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotalAmount(total);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING_PAYMENT);

        return orderRepository.save(order);
    }

    @Transactional
    public Order markAsPaid(String stripePaymentId) {
        Order order = orderRepository.findByStripePaymentId(stripePaymentId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + stripePaymentId));
        order.setStatus(Order.OrderStatus.PAID);
        order.setEstimatedReadyAt(LocalDateTime.now().plusMinutes(20));
        return orderRepository.save(order);
    }

    @Transactional
    public Order acceptOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setEstimatedReadyAt(LocalDateTime.now().plusMinutes(preparationMinutes));
        return orderRepository.save(order);
    }

    @Transactional
    public Order markAsReady(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(Order.OrderStatus.READY);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Order> getAllActiveOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void updateStripePaymentId(Long orderId, String paymentIntentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStripePaymentId(paymentIntentId);
        orderRepository.save(order);
    }
    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
        String random = String.valueOf((int)(Math.random() * 900) + 100);
        return "#" + date + "-" + random;
    }

}

