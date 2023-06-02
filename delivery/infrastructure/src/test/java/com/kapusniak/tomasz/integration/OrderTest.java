package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.openapi.model.PackageType;
import com.kapusniak.tomasz.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@TestPropertySource("classpath:application-integration-test.properties")
@SqlGroup(
        @Sql(
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:integration-test-scripts/cleanup.sql",
                        "classpath:integration-test-scripts/insert-data.sql"})
)
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should correctly get Order from database and verify" +
            " properties with Order from controller method")
    void getOrderExisting() throws Exception {
        // given
        Long orderId = 1L;
        Order order = orderService.findById(orderId);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/orders/" + orderId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    @DisplayName("should throw an exception when provided order id is not existing" +
            " in database for searching")
    void getOrderNonExisting() {
        // given
        Long orderId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/orders/" + orderId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should correctly return list Orders from database after executing" +
            " method from controller")
    void getAllOrders() throws Exception {
        // given
        List<Order> orderList = orderService.findAll();

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/orders"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id")
                        .value(orderList.get(0).getId()))
                .andExpect(jsonPath("$[1].id")
                        .value(orderList.get(1).getId()));


    }

    @Sql("classpath:integration-test-scripts/cleanup.sql")
    @Test
    @DisplayName("should correctly return empty list of Orders when table is empty" +
            "after executing method from controller")
    void getAllOrdersEmpty() throws Exception {
        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/orders"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("should save Order to database after executing method" +
            " from controller")
    void createOrder() throws Exception {
        // given
        Order order = new Order();
        order.setPackageType(PackageType.PARCEL);
        Customer customer = new Customer();
        customer.setId(1L);
        order.setCustomer(customer);

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.packageType").value(order.getPackageType().toString()));
    }

    @Test
    @DisplayName("should delete Order from database after executing" +
            " method from controller")
    void deleteOrderExisting() throws Exception {
        // given
        Long orderId = 1L;
        int sizeBeforeDeleting = orderService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/orders/" + orderId));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = orderService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided order id is not existing" +
            " in database for deleting")
    void deleteOrderNonExisting() {
        // given
        Long orderId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/orders/" + orderId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should save edited Order to database after executing method" +
            " from controller")
    void updateOrder() throws Exception {
        // given
        Long orderId = 1L;
        Order orderBeforeEdit = orderService.findById(orderId);
        PackageType newPackageType = PackageType.DOCUMENT;

        // and
        orderBeforeEdit.setPackageType(PackageType.DOCUMENT);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderBeforeEdit.getId()))
                .andExpect(jsonPath("$.packageType").value(newPackageType.toString()));

        // and
        Order orderAfterEdit = orderService.findById(orderId);
        assertThat(orderAfterEdit.getPackageType()).isEqualTo(newPackageType);
    }
}