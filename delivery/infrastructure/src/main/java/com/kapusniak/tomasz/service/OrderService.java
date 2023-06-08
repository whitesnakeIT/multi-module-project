package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.OrderEntityMapper;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderJpaRepository orderRepository;

    private final OrderEntityMapper orderEntityMapper;

    @Transactional
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

    public Order findByUuid(UUID orderUuid) {
        if (orderUuid == null) {
            throw new RuntimeException("Searching for order failed. Order uuid is null.");
        }
        return orderEntityMapper.mapToApiModel(orderRepository.findByUuid(orderUuid)
                .orElseThrow(RuntimeException::new));
    }

    @Transactional
    public void delete(UUID orderUuid) {
        if (orderUuid == null) {
            throw new RuntimeException("Deleting order failed. Order uuid is null.");
        }
        Order order = findByUuid(orderUuid);

        orderRepository.delete(orderEntityMapper.mapToEntity(order));
    }

    @Transactional
    public Order update(UUID uuid, Order order) {
        if (uuid == null) {
            throw new RuntimeException("Updating order failed. Order uuid is null.");
        }
        if (order == null) {
            throw new RuntimeException("Updating order failed. Order is null.");
        }

        Order orderFromDb = findByUuid(uuid);

        Order updatedOrder = updateFields(orderFromDb, order);

        OrderEntity updatedOrderEntity = orderRepository
                .save(orderEntityMapper.mapToEntity(updatedOrder));

        return orderEntityMapper.mapToApiModel(updatedOrderEntity);
    }

    private Order updateFields(Order orderFromDb, Order newOrder) {
        if (newOrder.getUuid() == null) {
            newOrder.setUuid(orderFromDb.getUuid());
        }
        if (!newOrder.getUuid().equals(orderFromDb.getUuid())) {
            throw new RuntimeException("Updating order fields failed. Different uuid's");
        }
        return newOrder;
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

    public List<Order> findAllByCustomerUuid(UUID customerUuid) {
        if (customerUuid == null) {
            throw new RuntimeException("Searching for customer orders failed. Customer uuid is null.");
        }

        return orderRepository
                .findAllByCustomerUuid(customerUuid)
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }
}
