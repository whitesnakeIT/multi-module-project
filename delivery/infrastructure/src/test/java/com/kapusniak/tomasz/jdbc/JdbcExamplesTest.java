package com.kapusniak.tomasz.jdbc;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.enums.CourierCompany;
import com.kapusniak.tomasz.jdbc.mapper.CourierRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.CustomerRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.OrderRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-jdbc-test.properties")
@ActiveProfiles("jdbc")
class JdbcExamplesTest {

    @Mock
    private CourierRowMapper courierRowMapper;
    @Mock
    private CustomerRowMapper customerRowMapper;
    @Mock
    private OrderRowMapper orderRowMapper;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private JdbcExamples jdbcExamples;

    private CustomerEntity customer;

    private CourierEntity courier;

    @BeforeEach
    public void setUp() {
        customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setEmail("testEmail");

        courier = new CourierEntity();
        courier.setId(1L);
        courier.setFirstName("courierFirstName");
        courier.setLastName("courierLastName");
        courier.setCourierCompany(CourierCompany.DPD);
        courier.setDeliveryList(List.of(new DeliveryEntity(), new DeliveryEntity()));
    }

    @Test
    @DisplayName("should correctly save one customer via JdbcTemplate")
    void insertCustomer() {

        // given
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String email = "testEmail";

        given(jdbcTemplate.update(any(String.class), any(String.class)))
                .willReturn(1);

        // when
        int result = jdbcExamples.insertCustomer(firstName, lastName, email);

        // then
        assertThat(result).isEqualTo(1);

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .update("INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, EMAIL) VALUES (? , ?, ?)",
                        firstName,
                        lastName,
                        email);
    }

    @Test
    @DisplayName("should return courier if exist based on courier id")
    void getCourierById() {

        // given
        given(jdbcTemplate.queryForObject(
                any(String.class),
                new Object[]{any(Object.class)},
                any(CourierRowMapper.class)))
                .willReturn(courier);

        Long courierId = 1L;

        // when
        CourierEntity courierById = jdbcExamples.getCourierById(courierId);

        // then
        assertThat(courierById.getId()).isNotNull();

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .queryForObject("SELECT * FROM COURIERS WHERE COURIER_ID = ?",
                        new Object[]{courierId},
                        courierRowMapper);
    }

    @Test
    @DisplayName("should return customer if exist (with orders), based on customer id")
    void getCustomerById() {

        // given
        given(jdbcTemplate.queryForObject(
                any(String.class),
                new Object[]{any(Object.class)},
                any(CustomerRowMapper.class)))
                .willReturn(customer);

        given(jdbcTemplate.query(
                any(String.class),
                any(OrderRowMapper.class)))
                .willReturn(List.of(new OrderEntity()));

        Long customerId = 1L;

        // when
        CustomerEntity customerById = jdbcExamples.getCustomerById(customerId);

        // then
        assertThat(customerById.getId()).isNotNull();

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .queryForObject("SELECT CUSTOMERS.* FROM CUSTOMERS WHERE CUSTOMER_ID = ?",
                        new Object[]{customerId},
                        customerRowMapper);

        then(jdbcTemplate)
                .should(times(1))
                .query("SELECT * FROM ORDERS WHERE CUSTOMER_ID = 1",
                        orderRowMapper);
    }

    @Test
    @DisplayName("should return list of all customers with their orders")
    void getCustomerList() {

        // given
        given(jdbcTemplate.query(
                any(String.class),
                any(CustomerRowMapper.class)))
                .willReturn(List.of(new CustomerEntity(), new CustomerEntity()));

        given(jdbcTemplate.query(
                anyString(),
                any(OrderRowMapper.class)))
                .willReturn(List.of(new OrderEntity()));

        // when
        List<CustomerEntity> customerList = jdbcExamples.getCustomerList();

        // then
        assertThat(customerList.size()).isEqualTo(2);

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .query("SELECT CUSTOMERS.* FROM CUSTOMERS",
                        customerRowMapper);

        then(jdbcTemplate)
                .should(times(2))
                .query("SELECT * FROM ORDERS WHERE CUSTOMER_ID = null",
                        orderRowMapper);
    }

    @Test
    @DisplayName("should return null instead of throwing an exception " +
            "when can't find customer by customer id ")
    void getCustomerByNonExistingId() {

        // given
        Long notExistingCustomerId = 1_000_000L;

        // when
        CustomerEntity customerNull = jdbcExamples.getCustomerById(notExistingCustomerId);

        //then
        assertThat(customerNull)
                .isNull();

    }

    @Test
    @DisplayName("should delete are rows based on schema name and table name")
    void deleteAllFrom() {

        // given
        String schemaName = "TEST";
        String tableName = "ORDERS";
        given(jdbcTemplate.update(
                any(String.class)))
                .willReturn(2);

        // when
        int result = jdbcExamples.deleteAllFrom(schemaName, tableName);

        // then
        assertThat(result).isEqualTo(2);

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .update("DELETE FROM " + schemaName + "." + tableName);
    }

    @Test
    @DisplayName("should return list of all orders")
    void getOrderList() {

        // given
        given(jdbcTemplate.query(
                any(String.class),
                any(OrderRowMapper.class)))
                .willReturn(List.of(new OrderEntity(), new OrderEntity()));

        // when
        List<OrderEntity> orderList = jdbcExamples.getOrderList();

        // then
        assertThat(orderList.size()).isEqualTo(2);

        // verifu
        then(jdbcTemplate)
                .should(times(1))
                .query("SELECT * FROM ORDERS",
                        orderRowMapper);
    }

    @Test
    void getCustomerOrderList() {

        // given
        Long customerId = 1L;
        given(jdbcTemplate.query(
                any(String.class),
                any(OrderRowMapper.class)))
                .willReturn(List.of(new OrderEntity(), new OrderEntity()));

        // when
        List<OrderEntity> customerOrderList = jdbcExamples.getCustomerOrderList(customerId);

        // then
        assertThat(customerOrderList.size()).isEqualTo(2);

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .query("SELECT * FROM ORDERS WHERE CUSTOMER_ID = " + customerId,
                        orderRowMapper);

    }

    @Test
    @DisplayName("should return order if exist based on order id ")
    void getOrderById() {

        // given
        Long orderId = 1L;


        given(jdbcTemplate.queryForObject(
                any(String.class),
                new Object[]{any(Object.class)},
                any(OrderRowMapper.class)))
                .willReturn(new OrderEntity());

        // when
        OrderEntity orderById = jdbcExamples.getOrderById(orderId);

        // then
        assertThat(orderById).isNotNull();

        // verify
        then(jdbcTemplate)
                .should(times(1))
                .queryForObject("SELECT * FROM ORDERS WHERE ORDER_ID = ?",
                        new Object[]{1L},
                        orderRowMapper);


    }

}