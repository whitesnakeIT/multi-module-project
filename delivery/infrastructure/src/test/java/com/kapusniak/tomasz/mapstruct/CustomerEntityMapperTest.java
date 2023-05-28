package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerEntityMapperTest {

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    public Customer prepareCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setEmail("test@test.com");
        customer.setOrders(List.of(new Order(), new Order()));

        return customer;
    }

    public CustomerEntity prepareCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setFirstName("testFirstName");
        customerEntity.setLastName("testLastName");
        customerEntity.setEmail("test@test.com");
        customerEntity.setOrders(List.of(new OrderEntity(), new OrderEntity()));

        return customerEntity;
    }

    @Test
    @DisplayName("should map from Customer to CustomerEntity with ignored" +
            " Order.Customer field to avoid cycle dependencies")
    void mapToEntity() {
        // given
        Customer customer = prepareCustomer();

        // when
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);

        // then
        assertThat(customerEntity.getId()).isEqualTo(customer.getId());
        assertThat(customerEntity.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(customerEntity.getLastName()).isEqualTo(customer.getLastName());
        assertThat(customerEntity.getEmail()).isEqualTo(customer.getEmail());

        assertThat(customerEntity.getOrders()).isNotNull();
        assertThat(customerEntity.getUuid()).isNotNull();
        assertThat(customerEntity.getOrders().get(0).getCustomer()).isNull();
        assertThat(customerEntity.getOrders().get(1).getCustomer()).isNull();
    }

    @Test
    @DisplayName("should map from CustomerEntity to Customer with ignored" +
            " Order.Customer field to avoid cycle dependencies")
    void mapToApiModel() {
        // given
        CustomerEntity customerEntity = prepareCustomerEntity();

        // when
        Customer customer = customerEntityMapper.mapToApiModel(customerEntity);

        // then
        assertThat(customer.getId()).isEqualTo(customerEntity.getId());
        assertThat(customer.getFirstName()).isEqualTo(customerEntity.getFirstName());
        assertThat(customer.getLastName()).isEqualTo(customerEntity.getLastName());
        assertThat(customer.getEmail()).isEqualTo(customerEntity.getEmail());

        assertThat(customer.getOrders()).isNotNull();
        assertThat(customer.getOrders().get(0).getCustomer()).isNull();
        assertThat(customer.getOrders().get(1).getCustomer()).isNull();

    }
}