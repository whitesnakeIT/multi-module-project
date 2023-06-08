package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.service.CustomerService;
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
import java.util.UUID;

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
public class CustomerTest {

    private static final UUID UUID_CUSTOMER_1 = UUID.fromString("28f60dc1-993a-4d08-ac54-850a1fefb6a3");
    private static final UUID ORDER_UUID_1 = UUID.fromString("29755321-c483-4a12-9f64-30a132038b70");


    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Customer prepareCustomer() {
        Customer customer = new Customer();
        Long testId = 1L;
        String testEmail = "test@email.com";
        String testFirstName = "testFirstName";
        String testLastName = "testLastName";
        customer.setId(testId);
        customer.setEmail(testEmail);
        customer.setFirstName(testFirstName);
        customer.setLastName(testLastName);
        customer.setUuid(UUID_CUSTOMER_1);

        Order order = orderService.findByUuid(ORDER_UUID_1);
//        order.setPreferredDeliveryDate(LocalDate.of(2023, 6, 7));
//        order.setPackageSize(LARGE);
//        order.setPackageType(DOCUMENT);
//        order.setSenderAddress("test sender address");
//        order.setReceiverAddress("test receiver address");
//        order.setUuid(UUID.randomUUID());
//        order.setId(1L);
//        order.setCustomer(customer);

        customer.addOrdersItem(order);


        return customer;
    }

    @Test
    @DisplayName("should correctly get Customer from database and verify" +
            " properties with Customer from controller method")
    void getCustomerExisting() throws Exception {
        // given
        UUID customerUuid = UUID_CUSTOMER_1;
        Customer customer = customerService.findByUuid(customerUuid);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/customers/" + customerUuid));


        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(customer.getUuid().toString()));
    }

    @Test
    @DisplayName("should throw an exception when provided customer uuid is not existing" +
            " in database for searching")
    void getCustomerNonExisting() {
        // given
        UUID customerUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/customers/" + customerUuid)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should correctly return list Customers from database after executing" +
            " method from controller")
    void getAllCustomers() throws Exception {
        // given
        List<Customer> customerList = customerService.findAll();

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/customers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uuid")
                        .value(customerList.get(0).getUuid().toString()))
                .andExpect(jsonPath("$[1].uuid")
                        .value(customerList.get(1).getUuid().toString()));


    }

    @Sql("classpath:integration-test-scripts/cleanup.sql")
    @Test
    @DisplayName("should correctly return empty list of Customers when table is empty" +
            "after executing method from controller")
    void getAllCustomersEmpty() throws Exception {
        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/customers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("should save Customer to database after executing method" +
            " from controller")
    void createCustomer() throws Exception {
        // given
        Customer customer = prepareCustomer();

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()));
    }

    @Test
    @DisplayName("should delete Customer from database after executing" +
            " method from controller")
    void deleteCustomerExisting() throws Exception {
        // given
        UUID customerUuid = UUID_CUSTOMER_1;
        int sizeBeforeDeleting = customerService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/customers/" + customerUuid));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = customerService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided customer uuid is not existing" +
            " in database for deleting")
    void deleteCustomerNonExisting() {
        // given
        UUID customerUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/customers/" + customerUuid)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should save edited Customer to database after executing method" +
            " from controller")
    void updateCustomer() throws Exception {
        // given
        UUID customerUuid = UUID_CUSTOMER_1;
        Customer customerBeforeEdit = customerService.findByUuid(customerUuid);
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";


        // and
        customerBeforeEdit.setFirstName(newFirstName);
        customerBeforeEdit.setLastName(newLastName);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/customers/" + customerUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(customerBeforeEdit.getUuid().toString()))
                .andExpect(jsonPath("$.firstName").value(newFirstName))
                .andExpect(jsonPath("$.lastName").value(newLastName));

        // and
        Customer customerAfterEdit = customerService.findByUuid(customerUuid);
        assertThat(customerAfterEdit.getFirstName()).isEqualTo(newFirstName);
        assertThat(customerAfterEdit.getLastName()).isEqualTo(newLastName);
    }
}