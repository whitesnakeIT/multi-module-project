package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderEntityMapperTest {

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    public Customer prepareCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("testEmail");
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setOrders(List.of(new Order(), new Order()));

        return customer;
    }

    public CustomerEntity prepareCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setEmail("testEmail");
        customerEntity.setFirstName("testFirstName");
        customerEntity.setLastName("testLastName");
        customerEntity.setOrders(List.of(new OrderEntity(), new OrderEntity()));

        return customerEntity;
    }

    public Order prepareOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setSenderAddress("testSenderAddress");
        order.setReceiverAddress("testReceiverAddress");
        order.setPackageType(PackageType.PARCEL);
        order.setPackageSize(PackageSize.SMALL);
        Customer customer = prepareCustomer();
        order.setCustomer(customer);

        return order;
    }

    public OrderEntity prepareOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setSenderAddress("testSenderAddress");
        orderEntity.setReceiverAddress("testReceiverAddress");
        orderEntity.setPackageType(PackageType.PARCEL);
        orderEntity.setPackageSize(PackageSize.SMALL);
        CustomerEntity customerEntity = prepareCustomerEntity();
        orderEntity.setCustomer(customerEntity);

        return orderEntity;
    }

    @Test
    @DisplayName("should map Order to OrderEntity with ignored Customer.Orders field to avoid cycle dependencies")
    public void mapToEntity() {
        // given
        Order order = prepareOrder();

        // when
        OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);

        // then
        assertThat(orderEntity.getId()).isEqualTo(order.getId());
        assertThat(orderEntity.getSenderAddress()).isEqualTo(order.getSenderAddress());
        assertThat(orderEntity.getReceiverAddress()).isEqualTo(order.getReceiverAddress());
        assertThat(orderEntity.getPackageType()).isEqualTo(order.getPackageType());
        assertThat(orderEntity.getPackageSize()).isEqualTo(order.getPackageSize());
        assertThat(orderEntity.getUuid()).isNotNull();

        assertThat(orderEntity.getCustomer()).isNotNull();
        assertThat(orderEntity.getCustomer().getOrders()).isNull();
    }

    @Test
    @DisplayName("should map OrderEntity to Order with ignored Customer.Orders field to avoid cycle dependencies")
    public void mapToApiModel() {
        // given
        OrderEntity orderEntity = prepareOrderEntity();

        // when
        Order order = orderEntityMapper.mapToApiModel(orderEntity);

        // then
        assertThat(order.getId()).isEqualTo(orderEntity.getId());
        assertThat(order.getSenderAddress()).isEqualTo(orderEntity.getSenderAddress());
        assertThat(order.getReceiverAddress()).isEqualTo(orderEntity.getReceiverAddress());
        assertThat(order.getPackageType()).isEqualTo(orderEntity.getPackageType());
        assertThat(order.getPackageSize()).isEqualTo(orderEntity.getPackageSize());

        assertThat(order.getCustomer()).isNotNull();
        assertThat(order.getCustomer().getOrders()).isNull();
    }

    @Test
    @DisplayName("should map OrderEntity to Order with ignored Customer field, method can be used in other mappers," +
            " to avoid cycle dependencies")
    public void shouldMapToApiModelWithoutCustomer() {
        // given
        OrderEntity orderEntity = prepareOrderEntity();

        // when
        Order order = orderEntityMapper.mapToApiModelWithoutCustomer(orderEntity);

        // then
        assertThat(order.getId()).isEqualTo(orderEntity.getId());
        assertThat(order.getSenderAddress()).isEqualTo(orderEntity.getSenderAddress());
        assertThat(order.getReceiverAddress()).isEqualTo(orderEntity.getReceiverAddress());
        assertThat(order.getPackageType()).isEqualTo(orderEntity.getPackageType());
        assertThat(order.getPackageSize()).isEqualTo(orderEntity.getPackageSize());

        assertThat(order.getCustomer()).isNull();
    }

    @Test
    @DisplayName("should map Order to OrderEntity with ignored Customer field, method can be used in other mappers," +
            " to avoid cycle dependencies")
    public void shouldMapToEntityWithoutCustomer() {
        // given
        Order order = prepareOrder();

        // when
        OrderEntity orderEntity = orderEntityMapper.mapToApiEntityWithoutCustomer(order);

        // then
        assertThat(orderEntity.getId()).isEqualTo(order.getId());
        assertThat(orderEntity.getSenderAddress()).isEqualTo(order.getSenderAddress());
        assertThat(orderEntity.getReceiverAddress()).isEqualTo(order.getReceiverAddress());
        assertThat(orderEntity.getPackageType()).isEqualTo(order.getPackageType());
        assertThat(orderEntity.getPackageSize()).isEqualTo(order.getPackageSize());

        assertThat(orderEntity.getUuid()).isNotNull();
        assertThat(orderEntity.getCustomer()).isNull();
    }
}
