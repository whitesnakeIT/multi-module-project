package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapstruct.OrderEntityMapper;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    Order order = new Order();
    OrderEntity orderEntity = new OrderEntity();

    List<Order> orderList = new ArrayList<>();
    List<OrderEntity> orderEntityList = new ArrayList<>();
    @Mock
    private OrderJpaRepository orderRepository;
    @Mock
    private OrderEntityMapper orderEntityMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setup() {
//        order = new Order();
        order.setPreferredDeliveryDate(LocalDate.of(2023, 5, 4));
        order.setPackageSize(PackageSize.LARGE);
        order.setPackageType(PackageType.DOCUMENT);
        order.setSenderAddress("new sender address");
        order.setReceiverAddress("new receiver address");

//        Customer customer = new Customer();
//        customer.setId(5L);
//        order.setCustomer(customer);

        orderEntityList.add(orderEntity);
        orderEntityList.add(orderEntity);

        orderEntity.setPreferredDeliveryDate(LocalDate.of(2023, 5, 4));
        orderEntity.setPackageSize(PackageSize.LARGE);
        orderEntity.setPackageType(PackageType.DOCUMENT);
        orderEntity.setSenderAddress("new sender address");
        orderEntity.setReceiverAddress("new receiver address");

//        CustomerEntity customerEntity = new CustomerEntity();
//        customerEntity.setId(5L);
//        order.setCustomerEntity(customer);

        orderList.add(order);
        orderList.add(order);

        when(orderEntityMapper
                .mapToEntity(any(Order.class)))
                .thenReturn(orderEntity);
        when(orderEntityMapper
                .mapToApiModel(any(OrderEntity.class)))
                .thenReturn(order);

    }

    @Test
    @DisplayName("should correctly save an Order entity exactly once")
    void save() {

        //given
        when(orderRepository
                .save(any(OrderEntity.class)))
                .thenReturn(orderEntity);

        // when
        Order result = orderService.save(order);

        // then
        then(orderRepository)
                .should(times(1))
                .save(orderEntityMapper.mapToEntity(order));

        // verify
        assertThat(order).isEqualTo(result);
    }

    @Test
    @DisplayName("should throw an exception when Order entity is null and check method invocations")
    void saveNull() {

        // given
        order = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.save(order));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Saving order failed. Order is null.");

        // verify
        then(orderRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of all orders with correct size and check method invocations")
    void findAll() {

        // given
        given(orderRepository.findAll())
                .willReturn(orderEntityList);

        // when
        List<Order> allOrders = orderService.findAll();

        // then
        assertThat(allOrders.size())
                .isEqualTo(2);

        // verify
        then(orderRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("should return order based on order id")
    void findById() {

        // given
        given(orderRepository.findById(
                anyLong()))
                .willReturn(Optional.of(orderEntity));
        Long orderId = 1L;

        // when
        Order orderById = orderService.findById(orderId);

        // then
        assertThat(orderById)
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when order id is null")
    void findByIdNull() {

        // given
        Long orderId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findById(orderId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for order failed. Order id is null.");

        // verify
        then(orderRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of orders with correct size based by package type")
    void findByPackageType() {

        // given
        given(orderRepository.findByPackageType(any()))
                .willReturn(orderEntityList);

        // when
        List<Order> ordersByPackageType = orderService.findByPackageType(PackageType.DOCUMENT);

        // then
        assertThat(ordersByPackageType.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should throw an exception when package type is null")
    void findByPackageTypeNull() {

        // given
        PackageType packageTypeNull = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findByPackageType(packageTypeNull));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for order failed. Package type is null.");
    }

    @Test
    @DisplayName("should return list of orders with correct size based by package size")
    void findByPackageSize() {

        // given
        given(orderRepository.findByPackageSize(any()))
                .willReturn(orderEntityList);

        // when
        List<Order> ordersByPackageSize = orderService.findByPackageSize(PackageSize.EXTRA_LARGE);

        // then
        assertThat(ordersByPackageSize.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should throw an exception when package size is null")
    void findByPackageSizeNull() {

        // given
        PackageSize packageSizeNull = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findByPackageSize(packageSizeNull));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for order failed. Package size is null.");
    }

    @Test
    @Disabled("need to fix open api generator, and mapstruct binding for child")
    @DisplayName("should return list of all customer orders based on customer id")
    void findAllByCustomerId() {

        // given
        Long customerId = 1L;
        given(orderRepository.findAllByCustomerId(any()))
                .willReturn(orderEntityList);

        // when
        List<Order> ordersByCustomerId = orderService.findAllByCustomerId(customerId);

        // then
        assertThat(ordersByCustomerId.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should throw an exception when customer id is null")
    void findAllByCustomerIdNull() {

        // given
        Long customerId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findAllByCustomerId(customerId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for customer orders failed. Customer id is null.");
    }

    @Test
    @DisplayName("should delete an order based on order id")
    void delete() {

        // given
        given(orderRepository.findById(
                anyLong()))
                .willReturn(Optional.of(orderEntity));
        Long orderId = 1L;

        // when
        orderService.delete(orderId);

        // then
        then(orderRepository)
                .should(times(1))
                .delete(orderEntityMapper.mapToEntity(order));
    }

    @Test
    @DisplayName("should throw an exception when order id is null")
    void deleteNull() {
        // given
        Long orderId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.delete(orderId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting order failed. Order id is null.");
    }

}