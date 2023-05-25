package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapstruct.OrderEntityMapper;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderJpaRepository orderRepository;

    private final OrderEntityMapper orderEntityMapper;

    public Order save(Order order) {
        if (order == null) {
            throw new RuntimeException("Saving order failed. Order is null.");
        }
        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
        OrderEntity savedEntity = orderRepository.save(orderEntity);

        return orderEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Order> findAll() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public Order findById(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Searching for order failed. Order id is null.");
        }
        return orderEntityMapper.mapToApiModel(orderRepository.findById(orderId)
                .orElseThrow(RuntimeException::new));
    }

    public void delete(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("Deleting order failed. Order id is null.");
        }
        Order order = findById(orderId);

        orderRepository.delete(orderEntityMapper.mapToEntity(order));
    }

    public Order update(Long id, Order order) {
        if (id == null) {
            throw new RuntimeException("Updati ng order failed. Order id is null.");
        }
        if (order == null) {
            throw new RuntimeException("Updating order failed. Order is null.");
        }

        Order orderFromDb = findById(id);

        // update

        OrderEntity updatedOrder = orderRepository
                .save(orderEntityMapper.mapToEntity(orderFromDb));

        return orderEntityMapper.mapToApiModel(updatedOrder);
    }

    public List<Order> findByPackageType(PackageType packageType) {
        if (packageType == null) {
            throw new RuntimeException("Searching for order failed. Package type is null.");
        }

        return orderRepository
                .findByPackageType(packageType)
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Order> findByPackageSize(PackageSize packageSize) {
        if (packageSize == null) {
            throw new RuntimeException("Searching for order failed. Package size is null.");
        }

        return orderRepository
                .findByPackageSize(packageSize)
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Order> findAllByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Searching for customer orders failed. Customer id is null.");
        }

        return orderRepository
                .findAllByCustomerId(customerId)
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }
}
