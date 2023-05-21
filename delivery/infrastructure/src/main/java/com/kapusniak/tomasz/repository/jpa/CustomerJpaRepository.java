package com.kapusniak.tomasz.repository.jpa;

import com.kapusniak.tomasz.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
}