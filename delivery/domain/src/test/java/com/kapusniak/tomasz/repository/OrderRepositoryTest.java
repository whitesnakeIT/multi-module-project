package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("should return list of orders with correct size based on package type")
    void findByPackageType() {

        //when
        List<Order> ordersByPackageType = orderRepository.findByPackageType(PackageType.DOCUMENT);

        //then
        then(ordersByPackageType.size())
                .isEqualTo(3);

        then(ordersByPackageType)
                .allMatch(order -> order.getPackageType().equals(PackageType.DOCUMENT));
    }

    @Test
    @DisplayName("should return empty list of orders based on package type")
    void findByPackageTypeEmpty() {

        // given
        orderRepository.deleteAll();

        // when
        List<Order> ordersByPackageType = orderRepository.findByPackageType(PackageType.DOCUMENT);

        // then
        then(ordersByPackageType)
                .isEmpty();
    }

    @Test
    @DisplayName("should return list of orders with correct size based on package size")
    void findByPackageSize() {

        //when
        List<Order> ordersByPackageSize = orderRepository.findByPackageSize(PackageSize.EXTRA_LARGE);

        //then
        then(ordersByPackageSize.size())
                .isEqualTo(1);

        then(ordersByPackageSize)
                .allMatch(order -> order.getPackageSize().equals(PackageSize.EXTRA_LARGE));
    }

    @Test
    @DisplayName("should return empty list of orders based on package size")
    void findByPackageSizeEmpty() {

        // given
        orderRepository.deleteAll();

        // when
        List<Order> ordersByPackageSize = orderRepository.findByPackageSize(PackageSize.EXTRA_LARGE);

        // then
        then(ordersByPackageSize)
                .isEmpty();
    }

    @Test
    @DisplayName("should return list of orders with correct size based on customer id")
    void findAllByCustomerIdExisting() {

        // when
        List<Order> ordersByCustomerId = orderRepository.findAllByCustomerId(1L);

        // then
        then(ordersByCustomerId.size())
                .isEqualTo(3);
    }
    @Test
    @DisplayName("should return empty list of orders based on customer id")
    void findAllByCustomerIdNotExisting() {

        // given
        orderRepository.deleteAll();

        // when
        List<Order> ordersByCustomerId = orderRepository.findAllByCustomerId(1L);

        // then
        then(ordersByCustomerId)
                .isEmpty();
    }
}