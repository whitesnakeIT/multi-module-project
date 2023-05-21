package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@TestPropertySource(locations = "/application-test.properties")
class OrderJpaRepositoryTest {

    @Autowired
    private OrderJpaRepository orderRepository;

    @Autowired
    private DeliveryJpaRepository deliveryRepository;

    @Test
    @DisplayName("should return list of orders with correct size based on package type")
    void findByPackageType() {

        //when
        List<OrderEntity> ordersByPackageType = orderRepository.findByPackageType(PackageType.DOCUMENT);

        //then
        then(ordersByPackageType.size())
                .isEqualTo(3);

        then(ordersByPackageType)
                .extracting(OrderEntity::getPackageType)
                .containsOnly(PackageType.DOCUMENT);
    }

    @Test
    @DisplayName("should return empty list of orders based on package type")
    void findByPackageTypeEmpty() {

        // given
        deliveryRepository.deleteAll();
        orderRepository.deleteAll();

        // when
        List<OrderEntity> ordersByPackageType = orderRepository.findByPackageType(PackageType.DOCUMENT);

        // then
        then(ordersByPackageType)
                .isEmpty();
    }

    @Test
    @DisplayName("should return list of orders with correct size based on package size")
    void findByPackageSize() {

        //when
        List<OrderEntity> ordersByPackageSize = orderRepository.findByPackageSize(PackageSize.EXTRA_LARGE);

        //then
        then(ordersByPackageSize.size())
                .isEqualTo(1);

        then(ordersByPackageSize)
                .extracting(OrderEntity::getPackageSize)
                .containsOnly(PackageSize.EXTRA_LARGE);
    }

    @Test
    @DisplayName("should return empty list of orders based on package size")
    void findByPackageSizeEmpty() {

        // given
        deliveryRepository.deleteAll();
        orderRepository.deleteAll();

        // when
        List<OrderEntity> ordersByPackageSize = orderRepository.findByPackageSize(PackageSize.EXTRA_LARGE);

        // then
        then(ordersByPackageSize)
                .isEmpty();
    }

    @Test
    @DisplayName("should return list of orders with correct size based on customer id")
    void findAllByCustomerIdExisting() {

        // when
        List<OrderEntity> ordersByCustomerId = orderRepository.findAllByCustomerId(1L);

        // then
        then(ordersByCustomerId.size())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("should return empty list of orders based on customer id")
    void findAllByCustomerIdNotExisting() {

        // given
        deliveryRepository.deleteAll();
        orderRepository.deleteAll();

        // when
        List<OrderEntity> ordersByCustomerId = orderRepository.findAllByCustomerId(1L);

        // then
        then(ordersByCustomerId)
                .isEmpty();
    }
}