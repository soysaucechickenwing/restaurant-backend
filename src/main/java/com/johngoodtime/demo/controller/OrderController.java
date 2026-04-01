package com.johngoodtime.demo.controller;

import com.johngoodtime.demo.model.Order;
import com.johngoodtime.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllActiveOrders());
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.acceptOrder(id));
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<Order> markReady(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markAsReady(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.updateStatus(id, Order.OrderStatus.CANCELLED));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.updateStatus(id, Order.OrderStatus.COMPLETED));
    }

}
