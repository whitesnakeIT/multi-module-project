package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CustomerEntityMapperTest {

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    public List<Order> prepareOrderList() {
        Order order1 = new Order();
        Order order2 = new Order();

        return List.of(order1, order2);
    }

    public Customer prepareCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setEmail("test@test.com");

        List<Order> orders = prepareOrderList();
        orders.forEach(order -> order.setCustomer(customer));
        customer.setOrders(orders);

        return customer;
    }

    public List<OrderEntity> prepareOrderEntityList() {
        OrderEntity orderEntity1 = new OrderEntity();
        OrderEntity orderEntity2 = new OrderEntity();

        return List.of(orderEntity1, orderEntity2);
    }

    public CustomerEntity prepareCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setFirstName("testFirstName");
        customerEntity.setLastName("testLastName");
        customerEntity.setEmail("test@test.com");

        List<OrderEntity> orderEntities = prepareOrderEntityList();
        orderEntities.forEach(orderEntity -> orderEntity.setCustomer(customerEntity));
        customerEntity.setOrders(orderEntities);

        return customerEntity;
    }

    @Test
    @DisplayName("should map from Customer to CustomerEntity with ignored" +
            " Order.Customer.Orders field to avoid cycle dependencies")
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

        assertThat(customerEntity.getOrders().get(0).getCustomer()).isNotNull();
        assertThat(customerEntity.getOrders().get(1).getCustomer()).isNotNull();

        assertThat(customerEntity.getOrders().get(0).getCustomer().getOrders()).isNull();
        assertThat(customerEntity.getOrders().get(1).getCustomer().getOrders()).isNull();
    }

    @Test
    @DisplayName("should map from CustomerEntity to Customer with ignored" +
            " Order.Customer.Orders field to avoid cycle dependencies")
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
        assertThat(customer.getOrders().get(0).getCustomer()).isNotNull();
        assertThat(customer.getOrders().get(1).getCustomer()).isNotNull();

        assertThat(customer.getOrders().get(0).getCustomer().getOrders()).isNull();
        assertThat(customer.getOrders().get(1).getCustomer().getOrders()).isNull();

    }
}