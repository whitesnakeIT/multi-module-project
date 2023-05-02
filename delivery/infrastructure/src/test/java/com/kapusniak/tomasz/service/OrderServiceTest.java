package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Customer;
import com.kapusniak.tomasz.entity.Order;
import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import com.kapusniak.tomasz.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    Order order = new Order();

    List<Order> orderList = new ArrayList<>();
    @Mock
    private OrderRepository orderRepository;
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

        Customer customer = new Customer();
        customer.setId(5L);
        order.setCustomer(customer);

        orderList.add(order);
        orderList.add(order);

    }

    @Test
    @DisplayName("should correctly save an Order entity exactly once")
    void save() {

        // when
        orderService.save(order);

        // then
        then(orderRepository)
                .should(times(1))
                .save(order);
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
                .willReturn(orderList);

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
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
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
        then(orderRepository).shouldHaveNoInteractions();

    }

    @Test
    @DisplayName("should return list of orders with correct size based by package type")
    void findByPackageType() {

        // given
        given(orderRepository.findByPackageType(any()))
                .willReturn(orderList);

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
                .willReturn(orderList);

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
    @DisplayName("should delete an order based on order id")
    void delete() {

        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        Long orderId = 1L;

        // when
        orderService.delete(orderId);

        // then
        then(orderRepository)
                .should(times(1))
                .delete(order);


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