package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.Order;

import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByPackageType(PackageType packageType);

    List<Order> findByPackageSize(PackageSize packageSize);

    List<Order> findAllByCustomerId(Long customerId);

}
