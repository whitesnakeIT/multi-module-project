package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CustomerController customerController = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    public CustomerEntity getCustomer() {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1L);
        customer1.setFirstName("John");
        return customer1;
    }

    public List<CustomerEntity> getCustomerList() {
        List<CustomerEntity> customers = new ArrayList<>();
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1L);
        customer1.setFirstName("John");
        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(2L);
        customer2.setFirstName("Tom");
        customers.add(customer1);
        customers.add(customer2);
        return customers;
    }

    @Test
    @DisplayName("should correctly return list of customers")
    public void getAllCustomers() throws Exception {
        List<CustomerEntity> customers = getCustomerList();
        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Tom"));

        verify(customerService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should correctly return customer based on customer id")
    public void getCustomerById() throws Exception {
        CustomerEntity customer = getCustomer();

        when(customerService.findById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(customerService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return created customer")
    public void createCustomer() throws Exception {
        CustomerEntity customer = getCustomer();
        when(customerService
                .save(any(CustomerEntity.class)))
                .thenReturn(getCustomer());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(customerService,
                times(1))
                .save(any(CustomerEntity.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return updated customer")
    public void updateCustomer() throws Exception {

        CustomerEntity customer = getCustomer();
        when(customerService.update(eq(1L),
                any(CustomerEntity.class))).thenReturn(customer);
        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(customerService,
                times(1))
                .update(eq(1L), any(CustomerEntity.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return no content after deleting customer")
    public void deleteCustomer() throws Exception {

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(customerService);
    }
}