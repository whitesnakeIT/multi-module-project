package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface OrderJpaRepository extends UuidRepository<OrderEntity, UUID> {

    List<OrderEntity> findByPackageType(PackageType packageType, Pageable pageable);

    List<OrderEntity> findByPackageSize(PackageSize packageSize, Pageable pageable);

    List<OrderEntity> findAllByCustomerUuid(UUID customerId, Pageable pageable);

//    @Override
//    @NonNull
//    Page<OrderEntity> findAll(@NonNull Pageable pageable);

//    Page<OrderEntity> findAllByIdIn(List<Long> orderIds, Pageable pageable);

}
