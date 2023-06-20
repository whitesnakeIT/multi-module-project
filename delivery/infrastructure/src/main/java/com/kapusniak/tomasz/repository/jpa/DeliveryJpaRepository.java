package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface DeliveryJpaRepository extends UuidRepository<DeliveryEntity, UUID> {

    List<DeliveryEntity> findAllByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    // for scheduler to delete
    List<DeliveryEntity> findAllByDeliveryStatus(DeliveryStatus deliveryStatus);

    List<DeliveryEntity> findAllByDeliveryStatusAndDeliveryTimeBefore(DeliveryStatus deliveryStatus, LocalDateTime deliveryTime);

    List<DeliveryEntity> findAllByDeliveryStatusAndDeliveryTimeBefore(DeliveryStatus deliveryStatus, LocalDateTime deliveryTime, Pageable pageable);

//    @Override
//    @NonNull
//    Page<DeliveryEntity> findAll(@NonNull Pageable pageable);

    Page<DeliveryEntity> findAllByCourierIdIn(List<Long> courierIds, Pageable pageable);

    Page<DeliveryEntity> findAllByOrderIdIn(List<Long> orderIds, Pageable pageable);
}