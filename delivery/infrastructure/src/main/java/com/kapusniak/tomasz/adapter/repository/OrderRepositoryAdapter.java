//package com.kapusniak.tomasz.adapter.repository;
//
//import com.kapusniak.tomasz.entity.OrderEntity;
//import com.kapusniak.tomasz.enums.PackageSize;
//import com.kapusniak.tomasz.enums.PackageType;
//import com.kapusniak.tomasz.mapstruct.OrderEntityMapper;
//import com.kapusniak.tomasz.model.Order;
//import com.kapusniak.tomasz.ports.repository.OrderRepository;
//import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class OrderRepositoryAdapter implements OrderRepository {
//
//    private final OrderJpaRepository orderJpaRepository;
//
//    private final OrderEntityMapper orderEntityMapper;
//
//    @Override
//    public Order save(Order order) {
//        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
//        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
//
//        return orderEntityMapper
//                .mapToModel(savedEntity);
//    }
//
//    @Override
//    public Order findById(Long id) {
//        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(id);
//
//        return orderEntityOptional
//                .map(orderEntityMapper::mapToModel)
//                .orElse(null);
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return orderJpaRepository
//                .findAll()
//                .stream()
//                .map(orderEntityMapper::mapToModel)
//                .toList();
//    }
//
//    @Override
//    public void delete(Order order) {
//        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
//        orderJpaRepository.delete(orderEntity);
//    }
//
//    @Override
//    public Order update(Order order) {
//        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
//        OrderEntity updatedEntity = orderJpaRepository.save(orderEntity);
//        return orderEntityMapper.mapToModel(updatedEntity);
//    }
//
//    @Override
//    public List<Order> findByPackageType(PackageType packageType) {
//        List<OrderEntity> orderEntityList = orderJpaRepository.findByPackageType(packageType);
//
//        return orderEntityList
//                .stream()
//                .map(orderEntityMapper::mapToModel)
//                .toList();
//    }
//
//    @Override
//    public List<Order> findByPackageSize(PackageSize packageSize) {
//        List<OrderEntity> orderEntityList = orderJpaRepository.findByPackageSize(packageSize);
//
//        return orderEntityList
//                .stream()
//                .map(orderEntityMapper::mapToModel)
//                .toList();
//    }
//
//    @Override
//    public List<Order> findAllByCustomerId(Long customerId) {
//        List<OrderEntity> orderEntityList = orderJpaRepository.findAllByCustomerId(customerId);
//
//        return orderEntityList
//                .stream()
//                .map(orderEntityMapper::mapToModel)
//                .toList();
//    }
//
//}
