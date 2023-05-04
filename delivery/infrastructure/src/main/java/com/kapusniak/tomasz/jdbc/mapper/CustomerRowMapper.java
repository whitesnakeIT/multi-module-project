package com.kapusniak.tomasz.jdbc.mapper;

import com.kapusniak.tomasz.entity.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

        Customer customer = new Customer();
        customer.setId(rs.getLong("CUSTOMER_ID"));
        customer.setFirstName(rs.getString("FIRST_NAME"));
        customer.setLastName(rs.getString("LAST_NAME"));
        customer.setEmail(rs.getString("EMAIL"));

        return customer;
    }
}