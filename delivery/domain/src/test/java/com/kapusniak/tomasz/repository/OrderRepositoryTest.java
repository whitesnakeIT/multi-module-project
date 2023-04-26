package com.kapusniak.tomasz.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("should return list of order with correct package type")
    void findByPackageType() {

    }

    @Test
    void findByPackageSize() {
    }

    @Test
    void findAllByCustomerId() {
    }
}