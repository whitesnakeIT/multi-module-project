package com.kapusniak.tomasz.jdbc;

import com.kapusniak.tomasz.entity.Customer;
import com.kapusniak.tomasz.entity.Order;
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

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setEmail("testEmail");
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
                .willReturn(List.of(new Order()));

        Long customerId = 1L;

        // when
        Customer customerById = jdbcExamples.getCustomerById(customerId);

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
                .willReturn(List.of(new Customer(), new Customer()));

        given(jdbcTemplate.query(
                anyString(),
                any(OrderRowMapper.class)))
                .willReturn(List.of(new Order()));

        // when
        List<Customer> customerList = jdbcExamples.getCustomerList();

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
                .willReturn(List.of(new Order(), new Order()));

        // when
        List<Order> orderList = jdbcExamples.getOrderList();

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
                .willReturn(List.of(new Order(), new Order()));

        // when
        List<Order> customerOrderList = jdbcExamples.getCustomerOrderList(customerId);

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
                .willReturn(new Order());

        // when
        Order orderById = jdbcExamples.getOrderById(orderId);

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