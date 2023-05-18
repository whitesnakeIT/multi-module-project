package com.kapusniak.tomasz.adapter.service;

import com.kapusniak.tomasz.adapter.repository.OrderRepositoryAdapter;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import com.kapusniak.tomasz.model.Order;
import com.kapusniak.tomasz.ports.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceAdapter implements OrderService {

    private final OrderRepositoryAdapter orderRepository;

    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new RuntimeException("Saving order failed. Order is null.");
        }

        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Searching for order failed. Order id is null.");
        }

        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void delete(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Deleting order failed. Order id is null.");
        }
        Order order = findById(orderId);
        orderRepository.delete(order);
    }

    @Override
    public Order update(Order order) {
        return null;
    }

    @Override
    public List<Order> findByPackageType(PackageType packageType) {
        if (packageType == null) {
            throw new RuntimeException("Searching for order failed. Package type is null.");
        }

        return orderRepository.findByPackageType(packageType);
    }

    @Override
    public List<Order> findByPackageSize(PackageSize packageSize) {
        if (packageSize == null) {
            throw new RuntimeException("Searching for order failed. Package size is null.");
        }

        return orderRepository.findByPackageSize(packageSize);
    }

    @Override
    public List<Order> findAllByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer orders failed. Customer id is null.");
        }

        return orderRepository.findAllByCustomerId(customerId);
    }
}
