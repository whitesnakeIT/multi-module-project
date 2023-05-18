package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
//@Transactional
public class OrderService {

    private final OrderJpaRepository orderRepository;

    public OrderEntity save(OrderEntity order) {
        if (order == null) {
            throw new RuntimeException("Saving order failed. Order is null.");
        }
        return orderRepository.save(order);
    }

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }

    public List<OrderEntity> findAllByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer orders failed. Customer id is null.");
        }
        return orderRepository.findAllByCustomerId(customerId);
    }

    public OrderEntity findById(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Searching for order failed. Order id is null.");
        }
        return orderRepository
                .findById(orderId)
                .orElseThrow(RuntimeException::new);
    }

    public List<OrderEntity> findByPackageType(PackageType packageType) {
        if (packageType == null) {
            throw new RuntimeException("Searching for order failed. Package type is null.");
        }
        return orderRepository.findByPackageType(packageType);
    }

    public List<OrderEntity> findByPackageSize(PackageSize packageSize) {
        if (packageSize == null) {
            throw new RuntimeException("Searching for order failed. Package size is null.");
        }
        return orderRepository.findByPackageSize(packageSize);
    }

    public void delete(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Deleting order failed. Order id is null.");
        }
        OrderEntity order = findById(orderId);
        orderRepository.delete(order);
    }

    public OrderEntity update(Long id, OrderEntity order) {
        if (id == null) {
            throw new RuntimeException("Updating order failed. Order id is null.");
        }
        if (order == null) {
            throw new RuntimeException("Updating order failed. Order is null.");
        }

        OrderEntity orderFromDb = findById(id);
        orderFromDb.setSenderAddress(order.getSenderAddress());
        orderFromDb.setReceiverAddress(order.getReceiverAddress());
        orderFromDb.setPackageType(order.getPackageType());
        orderFromDb.setPackageSize(order.getPackageSize());

        return orderRepository.save(orderFromDb);
    }

}
    