package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.OrderEntityMapper;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.service.PagingService.PAGE_SIZE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderJpaRepository orderRepository;

    private final OrderEntityMapper orderEntityMapper;

    private final CustomerService customerService;

    private final PagingService pagingService;

    @Transactional
    @CachePut(value = "orders", key = "#order.uuid")
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Saving order failed. Order is null.");
        }
        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
        OrderEntity savedEntity = orderRepository.save(orderEntity);

        return orderEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Order> findAll(Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);

        return orderRepository
                .findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public Order findByUuid(UUID orderUuid) {
        if (orderUuid == null) {
            throw new EntityNotFoundException("Searching for order failed. Order uuid is null.");
        }

        return orderEntityMapper.mapToApiModel(orderRepository.findByUuid(orderUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Searching for order failed. Unrecognized uuid " + orderUuid)));
    }

    @Transactional
    public void delete(UUID orderUuid) {
        if (orderUuid == null) {
            throw new IllegalArgumentException("Deleting order failed. Order uuid is null.");
        }
        Order order = findByUuid(orderUuid);

        orderRepository.delete(orderEntityMapper.mapToEntity(order));
    }

    @Transactional
    public Order update(UUID uuid, Order order) {
        if (uuid == null) {
            throw new IllegalArgumentException("Updating order failed. Order uuid is null.");
        }
        if (order == null) {
            throw new IllegalArgumentException("Updating order failed. Order is null.");
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
            throw new IllegalArgumentException("Updating order fields failed. Different uuid's");
        }

        return newOrder;
    }

    public List<Order> findByPackageType(PackageType packageType, Integer page) {
        if (packageType == null) {
            throw new EntityNotFoundException("Searching for order failed. Package type is null.");
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

        return orderRepository
                .findByPackageType(packageType, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Order> findByPackageSize(PackageSize packageSize, Integer page) {
        if (packageSize == null) {
            throw new EntityNotFoundException("Searching for order failed. Package size is null.");
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

        return orderRepository
                .findByPackageSize(packageSize, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Order> findAllByCustomerUuid(UUID customerUuid, Integer page) {
        if (customerUuid == null) {
            throw new EntityNotFoundException("Searching for customer orders failed. Customer uuid is null.");
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

        return orderRepository
                .findAllByCustomerUuid(customerUuid, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream().map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Order> findAllByIdIn(List<Long> orderIds, Integer page) {
        if (orderIds == null) {
            throw new RuntimeException();
        }
        if (page == null) {
            throw new RuntimeException();
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

//        return orderRepository.findAllByIdIn(orderIds, PageRequest.of(pageNumber, PAGE_SIZE))
        return orderRepository.findAllPagedByIdIn(orderIds, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(orderEntityMapper::mapToApiModel)
                .toList();
    }

    public List<OrderEntity> findAllEntitiesByIdIn(List<Long> orderIds, Integer page) {
        if (orderIds == null) {
            throw new RuntimeException();
        }

        Integer pageNumber = pagingService.validatePageNumber(page);

        List<OrderEntity> orderEntities = orderRepository
//            .findAllByIdIn(orderIds, PageRequest.of(pageNumber, PAGE_SIZE));
                .findAllPagedByIdIn(orderIds, PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();

//    List<Long> customerIds = orderEntities.stream()
//            .map(order -> order.getCustomer().getId())
//            .toList();

//    List<CustomerEntity> customerEntities = customerService.findAllEntitiesByIdIn(customerIds, page);
//
//    orderEntities.forEach(orderEntity ->
//            orderEntity.setCustomer(
//                    customerService.extractCustomersEntities(
//                            customerEntities, orderEntity
//                    )
//            ));

        return orderEntities;
    }

    public Order extractOrder(List<Order> orders, Long deliveryId) {
        if (orders == null) {
            throw new RuntimeException();
        }
        if (deliveryId == null) {
            throw new RuntimeException();
        }

        return orders.stream()
                .filter(order -> order.getId().equals(deliveryId))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public OrderEntity extractOrderEntities(List<OrderEntity> orderEntities, Long deliveryId) {
        if (orderEntities == null) {
            throw new RuntimeException();
        }
        if (deliveryId == null) {
            throw new RuntimeException();
        }

        return orderEntities.stream()
                .filter(orderEntity -> orderEntity.getDelivery().getId().equals(deliveryId)) // tu jest zjebane bo nie moge orderEntity.getDelivery().getId();
                .findFirst().orElseThrow(RuntimeException::new);                                             // a i tak chyba by wtedy bylo n+1
    }


}
