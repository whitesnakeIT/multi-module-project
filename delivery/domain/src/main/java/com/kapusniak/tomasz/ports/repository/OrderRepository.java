//package com.kapusniak.tomasz.ports.repository;
//
//import com.kapusniak.tomasz.enums.PackageSize;
//import com.kapusniak.tomasz.enums.PackageType;
//import com.kapusniak.tomasz.model.Order;
//
//import java.util.List;
//
//public interface OrderRepository {
//    Order save(Order order);
//
//    Order findById(Long id);
//
//    List<Order> findAll();
//
//    void delete(Order order);
//
//    Order update(Order order);
//
//    List<Order> findByPackageType(PackageType packageType);
//
//    List<Order> findByPackageSize(PackageSize packageSize);
//
//    List<Order> findAllByCustomerId(Long customerId);
//
//}
