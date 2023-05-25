package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByPackageType(PackageType packageType);

    List<OrderEntity> findByPackageSize(PackageSize packageSize);

    List<OrderEntity> findAllByCustomerId(Long customerId);

}
