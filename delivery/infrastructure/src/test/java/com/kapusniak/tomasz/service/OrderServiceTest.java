package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.OrderEntityMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.repository.jpa.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.UUID;

import static com.kapusniak.tomasz.openapi.model.PackageSize.EXTRA_LARGE;
import static com.kapusniak.tomasz.openapi.model.PackageSize.LARGE;
import static com.kapusniak.tomasz.openapi.model.PackageType.DOCUMENT;
import static com.kapusniak.tomasz.openapi.model.PackageType.PARCEL;
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

    Customer customer = new Customer();

    CustomerEntity customerEntity = new CustomerEntity();
    @Mock
    private OrderJpaRepository orderRepository;
    @Mock
    private OrderEntityMapper orderEntityMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setup() {
        order.setId(1L);
        order.setPreferredDeliveryDate(LocalDate.of(2023, 5, 4));
        order.setPackageSize(LARGE);
        order.setPackageType(DOCUMENT);
        order.setSenderAddress("new sender address");
        order.setReceiverAddress("new receiver address");


        customer.setId(5L);
        order.setCustomer(customer);

        orderEntityList.add(orderEntity);
        orderEntityList.add(orderEntity);

        orderEntity.setId(1L);
        orderEntity.setPreferredDeliveryDate(LocalDate.of(2023, 5, 4));
        orderEntity.setPackageSize(LARGE);
        orderEntity.setPackageType(DOCUMENT);
        orderEntity.setSenderAddress("new sender address");
        orderEntity.setReceiverAddress("new receiver address");


        customerEntity.setId(5L);
        order.setCustomer(customer);

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
    void findByUuid() {

        // given
        given(orderRepository.findByUuid(
                any(UUID.class)))
                .willReturn(Optional.of(orderEntity));
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");

        // when
        Order orderByUuid = orderService.findByUuid(orderUuid);

        // then
        assertThat(orderByUuid)
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when order id is null")
    void findByUuidNull() {

        // given
        UUID orderUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findByUuid(orderUuid));

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
        List<Order> ordersByPackageType = orderService.findByPackageType(DOCUMENT);

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
        List<Order> ordersByPackageSize = orderService.findByPackageSize(EXTRA_LARGE);

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
    @DisplayName("should return list of all customer orders based on customer id")
    void findAllByCustomerUuid() {

        // given
        UUID customerUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        given(orderRepository.findAllByCustomerUuid(any()))
                .willReturn(orderEntityList);

        // when
        List<Order> ordersByCustomerUuid = orderService.findAllByCustomerUuid(customerUuid);

        // then
        assertThat(ordersByCustomerUuid.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should throw an exception when customer id is null")
    void findAllByCustomerIdNull() {

        // given
        UUID customerUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.findAllByCustomerUuid(customerUuid));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for customer orders failed. Customer id is null.");
    }

    @Test
    @DisplayName("should delete an order based on order id")
    void delete() {

        // given
        given(orderRepository.findByUuid(
                any(UUID.class)))
                .willReturn(Optional.of(orderEntity));
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");

        // when
        orderService.delete(orderUuid);

        // then
        then(orderRepository)
                .should(times(1))
                .delete(orderEntityMapper.mapToEntity(order));
    }

    @Test
    @DisplayName("should throw an exception when order id is null")
    void deleteNull() {
        // given
        UUID orderUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.delete(orderUuid));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting order failed. Order id is null.");
    }

    @Test
    @DisplayName("should throw an exception when id is null")
    void updateNullUuid() {
        // given
        UUID orderUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                orderService.update(orderUuid, order));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating order failed. Order id is null.");
    }

    @Test
    @DisplayName("should throw an exception when order is null")
    void updateNullOrder() {
        // given
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        Order order = null;

        // when
        Throwable thrown = catchThrowable(() ->
                orderService.update(orderUuid, order));

        // then
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating order failed. Order is null.");
    }

    @Test
    @DisplayName("should throw an exception when newOrder's id doesn't match orderFromDb's id")
    void updateIdMissMatch() {
        // given
        Order newOrder = new Order();
        UUID oldUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        UUID newUuid = UUID.fromString("9137383a-1574-4981-bf7e-3b05182fcf13");
        newOrder.setUuid(newUuid);

        // and
        when(orderRepository.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(orderEntity));
        // when
        Throwable throwable = catchThrowable(() ->
                orderService.update(oldUuid, newOrder));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating order fields failed. Different id's");
    }

    @Test
    @DisplayName("should correctly update order when valid id and order are provided")
    void shouldUpdateOrder() {
        // given
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        Order changedOrder = prepareOrderForEdit();
        OrderEntity changedOrderEntity = prepareOrderEntityForEdit();

        // and
        when(orderRepository.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class)))
                .thenReturn(changedOrderEntity);

        //and
        when(orderEntityMapper
                .mapToApiModel(changedOrderEntity))
                .thenReturn(changedOrder);

        // when
        Order updatedOrder = orderService.update(orderUuid, changedOrder);

        // then
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getUuid()).isEqualTo(changedOrder.getUuid());
        assertThat(updatedOrder.getSenderAddress()).isEqualTo(changedOrder.getSenderAddress());
        assertThat(updatedOrder.getReceiverAddress()).isEqualTo(changedOrder.getReceiverAddress());
        assertThat(updatedOrder.getPackageSize()).isEqualTo(changedOrder.getPackageSize());
        assertThat(updatedOrder.getPackageType()).isEqualTo(changedOrder.getPackageType());
        assertThat(updatedOrder.getPreferredDeliveryDate()).isEqualTo(changedOrder.getPreferredDeliveryDate());

        assertThat(updatedOrder.getCustomer().getUuid()).isEqualTo(changedOrder.getCustomer().getUuid());

        // verify
        then(orderRepository)
                .should(times(1))
                .save(orderEntity);
    }

    private Order prepareOrderForEdit() {
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        String newSenderAddress = "newSenderAddress";
        String newReceiverAddress = "newReceiverAddress";
        PackageType newPackageType = PARCEL;
        PackageSize newPackageSize = EXTRA_LARGE;
        LocalDate newPreferredDeliveryDate = LocalDate.of(2023, 5, 28);

        UUID newCustomerUuid = UUID.fromString("9137383a-1574-4981-bf7e-3b05182fcf13");
        Order changedOrder = new Order();
        changedOrder.setUuid(orderUuid);
        changedOrder.setSenderAddress(newSenderAddress);
        changedOrder.setReceiverAddress(newReceiverAddress);
        changedOrder.setPackageSize(newPackageSize);
        changedOrder.setPackageType(newPackageType);
        changedOrder.setPreferredDeliveryDate(newPreferredDeliveryDate);
        customer.setUuid(newCustomerUuid);
        changedOrder.setCustomer(customer);

        return changedOrder;
    }

    private OrderEntity prepareOrderEntityForEdit() {
        UUID orderUuid = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");
        String newSenderAddress = "newSenderAddress";
        String newReceiverAddress = "newReceiverAddress";
        PackageType newPackageType = PARCEL;
        PackageSize newPackageSize = EXTRA_LARGE;
        LocalDate newPreferredDeliveryDate = LocalDate.of(2023, 5, 28);

        UUID newCustomerUuid = UUID.fromString("28f60dc1-993a-4d08-ac54-850a1fefb6a3");
        OrderEntity changedOrderEntity = new OrderEntity();
        changedOrderEntity.setUuid(orderUuid);
        changedOrderEntity.setSenderAddress(newSenderAddress);
        changedOrderEntity.setReceiverAddress(newReceiverAddress);
        changedOrderEntity.setPackageSize(newPackageSize);
        changedOrderEntity.setPackageType(newPackageType);
        changedOrderEntity.setPreferredDeliveryDate(newPreferredDeliveryDate);
        customerEntity.setUuid(newCustomerUuid);
        changedOrderEntity.setCustomer(customerEntity);

        return changedOrderEntity;
    }
}