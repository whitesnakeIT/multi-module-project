package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface CourierJpaRepository extends UuidRepository<CourierEntity, UUID> {
//    @Override
//    @NonNull
//    Page<CourierEntity> findAll(@NonNull Pageable pageable);

}