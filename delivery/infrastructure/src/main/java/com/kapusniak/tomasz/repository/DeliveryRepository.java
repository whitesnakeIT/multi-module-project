package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}