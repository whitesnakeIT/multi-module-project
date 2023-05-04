package com.kapusniak.tomasz.jdbc;

import com.kapusniak.tomasz.entity.Courier;
import com.kapusniak.tomasz.entity.Customer;
import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.jdbc.mapper.CourierRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.CustomerRowMapper;
import com.kapusniak.tomasz.jdbc.mapper.OrderRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbcExamples {


    private final CourierRowMapper courierRowMapper;
    private final CustomerRowMapper customerRowMapper;
    private final OrderRowMapper orderRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public List<Order> getOrderList() {
        String query = "SELECT * FROM ORDERS";
        return jdbcTemplate.query(query, orderRowMapper);
    }

    public List<Order> getCustomerOrderList(Long customerId) {
        String query = "SELECT * FROM ORDERS WHERE CUSTOMER_ID = " + customerId;
        return jdbcTemplate.query(query, orderRowMapper);
    }

    public Order getOrderById(Long orderId) {
        String query = "SELECT * FROM ORDERS WHERE ORDER_ID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{orderId}, orderRowMapper);
    }

    public int insertCustomer(String firstName, String lastName, String email) {
        String query = "INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, EMAIL) VALUES (? , ?, ?)";
        return jdbcTemplate.update(query,
                firstName, lastName, email);
    }

    public Customer getCustomerById(Long customerId) {
        String query = "SELECT CUSTOMERS.* " +
                "FROM CUSTOMERS " +
                "WHERE CUSTOMER_ID = ?";

        Customer customer = null;
        try {
            customer = jdbcTemplate.
                    queryForObject(query, new Object[]{customerId}, customerRowMapper);
            if (customer != null) {
                customer.setOrders(getCustomerOrderList(customerId));
            }

        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
        return customer;
    }

    public Courier getCourierById(Long courierId) {
        String query = "SELECT * FROM COURIERS WHERE COURIER_ID = ?";

        return jdbcTemplate.
                queryForObject(query, new Object[]{courierId}, courierRowMapper);
    }


    public List<Customer> getCustomerList() {
        String query = "SELECT CUSTOMERS.* " +
                "FROM CUSTOMERS";

        List<Customer> customers = jdbcTemplate.query(query, customerRowMapper);
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
