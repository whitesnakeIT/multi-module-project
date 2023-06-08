package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface DeliveryJpaRepository extends UuidRepository<DeliveryEntity, UUID> {

}