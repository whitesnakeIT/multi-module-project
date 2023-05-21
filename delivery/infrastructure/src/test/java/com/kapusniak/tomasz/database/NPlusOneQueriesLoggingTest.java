package com.kapusniak.tomasz.database;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest("hibernate.query.interceptor.error-level=ERROR")
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
public class NPlusOneQueriesLoggingTest {

    private static final Logger logger =
            LoggerFactory.getLogger(NPlusOneQueriesLoggingTest.class);
    @Autowired
    private OrderJpaRepository orderRepository;

    @Test
    void logTest() {
        // Get all the orders from the database
        // -> Triggers 1 query
        List<OrderEntity> orders = orderRepository.findAll();

        orders.forEach(order -> logger.info(
                order.getCustomer().getFirstName()
                        + " packaging type: "
                        + order.getPackageType()));

    }

    @Test
    void nPlusOneQueriesDetection_isLoggingWhenDetectingNPlusOneQueries() {
        // Fetch the orders without the customers
        List<OrderEntity> orders = orderRepository.findAll();

        // Trigger N+1 queries
        List<String> names = orders.stream()
                .map(order -> order.getCustomer().getFirstName())
                .toList();
    }
}

