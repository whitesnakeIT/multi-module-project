package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.OrdersApi;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        Order save = orderService.save(order);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long orderId) {
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
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long orderId) {
        Order order = orderService.findById(orderId);

        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Long orderId, @RequestBody @Valid Order order) {
        Order update = orderService.update(orderId, order);

        return ResponseEntity.ok(update);
    }
}
