package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.repository.UuidRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface CustomerJpaRepository extends UuidRepository<CustomerEntity, UUID> {

//    @Override
//    @NonNull
//    Page<CustomerEntity> findAll(@NonNull Pageable pageable);

//    List<CustomerEntity> findAllEntitiesByIdIn(List<Long> customerIds, Pageable pageable);
}