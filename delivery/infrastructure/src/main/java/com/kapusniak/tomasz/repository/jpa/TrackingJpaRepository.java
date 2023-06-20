package com.kapusniak.tomasz.repository.jpa;


import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface TrackingJpaRepository extends UuidRepository<TrackingEntity, UUID> {

//    @Override
//    @NonNull
//    Page<TrackingEntity> findAll(@NonNull Pageable pageable);
}
