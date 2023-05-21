package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.OrdersApi;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order save = orderService.save(order);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long orderId) {
        orderService.delete(orderId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.findAll();

        return ResponseEntity.ok(orderList);
    }

    @Override
    public ResponseEntity<Order> getOrder(Long orderId) {
        Order order = orderService.findById(orderId);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Order> updateOrder(Long id, Order order) {
        return null;
    }
}
