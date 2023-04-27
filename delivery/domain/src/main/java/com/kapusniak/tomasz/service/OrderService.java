package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;

import com.kapusniak.tomasz.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
//@Transactional

public class OrderService {

    private final OrderRepository orderRepository;

    public void save(Order order) {
        if (order == null) {
            throw new RuntimeException("Saving order failed. Order is null.");
        }
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer orders failed. Order id is null.");
        }
        return orderRepository.findAllByCustomerId(customerId);
    }

    public Order findById(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Searching for order failed. Order id is null.");
        }
        return orderRepository
                .findById(orderId)
                .orElseThrow(RuntimeException::new);
    }

    public List<Order> findByPackageType(PackageType packageType) {
        if (packageType == null) {
            throw new RuntimeException("Searching for order failed. Package type is null.");
        }
        return orderRepository.findByPackageType(packageType);
    }

    public List<Order> findByPackageSize(PackageSize packageSize) {
        if (packageSize == null) {
            throw new RuntimeException("Searching for order failed. Package size is null.");
        }
        return orderRepository.findByPackageSize(packageSize);
    }

    public void delete(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Deleting order failed. Order id is null.");
        }
        Order order = findById(orderId);
        orderRepository.delete(order);
    }
}
