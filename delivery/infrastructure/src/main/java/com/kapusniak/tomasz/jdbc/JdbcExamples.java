package com.kapusniak.tomasz.jdbc;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.jdbc.mapper.CourierRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.CustomerRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.OrderRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JdbcExamples {


    private final CourierRowMapper courierRowMapper;
    private final CustomerRowMapper customerRowMapper;
    private final OrderRowMapper orderRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public List<OrderEntity> getOrderList() {
        String query = "SELECT * FROM ORDERS";
        return jdbcTemplate.query(query, orderRowMapper);
    }

    public List<OrderEntity> getCustomerOrderList(Long customerId) {
        String query = "SELECT * FROM ORDERS WHERE CUSTOMER_ID = " + customerId;
        return jdbcTemplate.query(query, orderRowMapper);
    }

    public OrderEntity getOrderByUuid(UUID orderUuid) {
        String query = "SELECT * FROM ORDERS WHERE UUID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{orderUuid}, orderRowMapper);
    }

    public int insertCustomer(String firstName, String lastName, String email) {
        String query = "INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, EMAIL) VALUES (? , ?, ?)";
        return jdbcTemplate.update(query,
                firstName, lastName, email);
    }

    public CustomerEntity getCustomerByUuid(UUID customerUuid) {
        String query = "SELECT CUSTOMERS.* " +
                "FROM CUSTOMERS " +
                "WHERE UUID = ?";

        CustomerEntity customer = null;
        try {
            customer = jdbcTemplate.
                    queryForObject(query, new Object[]{customerUuid}, customerRowMapper);
            if (customer != null) {
                customer.setOrders(getCustomerOrderList(customer.getId()));
            }

        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
        return customer;
    }

    public CourierEntity getCourierByUuid(UUID courierUuid) {
        String query = "SELECT * FROM COURIERS WHERE UUID = ?";

        return jdbcTemplate.
                queryForObject(query, new Object[]{courierUuid}, courierRowMapper);
    }


    public List<CustomerEntity> getCustomerList() {
        String query = "SELECT CUSTOMERS.* " +
                "FROM CUSTOMERS";

        List<CustomerEntity> customers = jdbcTemplate.query(query, customerRowMapper);
        if (!customers.isEmpty()) {
            customers.forEach(
                    customer -> customer.setOrders(
                            getCustomerOrderList(
                                    customer.getId())));
        }

        return customers;
    }

    public int deleteAllFrom(String schemaName, String tableName) {
        return jdbcTemplate.update("DELETE FROM " + schemaName + "." + tableName);
    }


}
