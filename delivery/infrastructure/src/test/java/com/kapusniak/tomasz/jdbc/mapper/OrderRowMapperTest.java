package com.kapusniak.tomasz.jdbc.mapper;

import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("jdbc")
@TestPropertySource("classpath:application-jdbc-test.properties")
class OrderRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @Autowired
    private OrderRowMapper orderRowMapper;

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getLong("ORDER_ID")).thenReturn(1L);
        when(resultSet.getString("SENDER_ADDRESS")).thenReturn("testSenderAddress");
        when(resultSet.getString("RECEIVER_ADDRESS")).thenReturn("testReceiverAddress");
        when(resultSet.getString("PACKAGE_TYPE")).thenReturn("DOCUMENT");
        when(resultSet.getString("PACKAGE_SIZE")).thenReturn("SMALL");
        when(resultSet.getDate("PREFERRED_DELIVERY_DATE")).thenReturn(Date.valueOf(LocalDate.of(2023, 5, 4)));
    }

    @Test
    @DisplayName("should correctly map resultSet for Customer object")
    void mapRow() throws SQLException {

        // when
        Order order = orderRowMapper.mapRow(resultSet, 1);

        // then
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("testSenderAddress", order.getSenderAddress());
        assertEquals("testReceiverAddress", order.getReceiverAddress());
        assertEquals(PackageType.DOCUMENT, order.getPackageType());
        assertEquals(PackageSize.SMALL, order.getPackageSize());
        assertEquals(LocalDate.of(2023, 5, 4), order.getPreferredDeliveryDate());

    }
}