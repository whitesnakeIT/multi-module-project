package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface CourierRepository extends JpaRepository<Courier, Long> {
}