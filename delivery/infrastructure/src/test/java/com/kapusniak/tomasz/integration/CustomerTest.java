package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.service.CustomerService;
import org.junit.jupiter.api.Disabled;
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
public class CustomerTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should correctly get Customer from database and verify" +
            " properties with Customer from controller method")
    void getCustomerExisting() throws Exception {
        // given
        Long customerId = 1L;
        Customer customer = customerService.findById(customerId);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/customers/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId()));
    }

    @Test
    @DisplayName("should throw an exception when provided customer id is not existing" +
            " in database for searching")
    void getCustomerNonExisting() {
        // given
        Long customerId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/customers/" + customerId)
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
                .andExpect(jsonPath("$[0].id")
                        .value(customerList.get(0).getId()))
                .andExpect(jsonPath("$[1].id")
                        .value(customerList.get(1).getId()));


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
    @DisplayName("should save edited Customer to database after executing method" +
            " from controller")
    void createCustomer() throws Exception {
        // given
        Customer customer = new Customer();
        customer.setFirstName("tom");

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()));
    }

    @Test
    @DisplayName("should delete Customer from database after executing" +
            " method from controller")
    void deleteCustomerExisting() throws Exception {
        // given
        Long customerId = 1L;
        int sizeBeforeDeleting = customerService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/customers/" + customerId));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = customerService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided customer id is not existing" +
            " in database for deleting")
    void deleteCustomerNonExisting() {
        // given
        Long customerId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/customers/" + customerId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Disabled
    @DisplayName("should save edited Customer to database after executing method" +
            " from controller")
    void updateCustomer() throws Exception {
        // given
        Long customerId = 1L;
        Customer customerBeforeEdit = customerService.findById(customerId);
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";


        // and
        customerBeforeEdit.setFirstName(newFirstName);
        customerBeforeEdit.setLastName(newLastName);
        customerBeforeEdit.getOrders().forEach(order -> order.setCustomer(customerBeforeEdit));

// when
        ResultActions result = mockMvc
                .perform(put("/api/v1/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerBeforeEdit.getId()))
                .andExpect(jsonPath("$.firstName").value(newFirstName))
                .andExpect(jsonPath("$.lastName").value(newLastName));

        // and
        Customer customerAfterEdit = customerService.findById(customerId);
        assertThat(customerAfterEdit.getFirstName()).isEqualTo(newFirstName);
        assertThat(customerAfterEdit.getLastName()).isEqualTo(newLastName);
    }
}