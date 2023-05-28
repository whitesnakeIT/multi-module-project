package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.service.CustomerService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    public Customer getCustomer() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("John");

        return customer1;
    }

    public List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("John");
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Tom");
        customers.add(customer1);
        customers.add(customer2);

        return customers;
    }

    @Test
    @DisplayName("should correctly return list of customers")
    public void getAllCustomers() throws Exception {
        // given
        List<Customer> customers = getCustomerList();
        when(customerService.findAll())
                .thenReturn(customers);

        // when
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Tom"));

        // then
        verify(customerService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should correctly return customer based on customer id")
    public void getCustomerById() throws Exception {
        // given
        Customer customer = getCustomer();
        when(customerService.findById(1L))
                .thenReturn(customer);

        // when
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        // then
        verify(customerService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return created customer")
    public void createCustomer() throws Exception {
        // given
        Customer customer = getCustomer();
        when(customerService
                .save(ArgumentMatchers.any(Customer.class)))
                .thenReturn(getCustomer());

        // when
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        // then
        verify(customerService,
                times(1))
                .save(ArgumentMatchers.any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @Disabled
    @DisplayName("should return updated customer")
    public void updateCustomer() throws Exception {
        // given
        Customer customer = getCustomer();
        when(customerService.update(ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(Customer.class)))
                .thenReturn(customer);

        // when
        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        // then
        verify(customerService,
                times(1))
                .update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return no content after deleting customer")
    public void deleteCustomer() throws Exception {
        // when
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        // then
        verify(customerService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(customerService);
    }
}