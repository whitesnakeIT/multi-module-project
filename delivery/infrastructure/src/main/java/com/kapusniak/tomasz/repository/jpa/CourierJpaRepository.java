package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface CourierJpaRepository extends JpaRepository<CourierEntity, Long> {
}