package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}