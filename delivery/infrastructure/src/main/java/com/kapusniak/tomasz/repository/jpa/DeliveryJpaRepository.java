package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {
}