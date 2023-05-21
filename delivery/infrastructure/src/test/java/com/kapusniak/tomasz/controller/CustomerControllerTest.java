package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

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
        List<Customer> customers = getCustomerList();
        Mockito.when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Tom"));

        Mockito.verify(customerService,
                        Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should correctly return customer based on customer id")
    public void getCustomerById() throws Exception {
        Customer customer = getCustomer();

        Mockito.when(customerService.findById(1L)).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(customerService,
                        Mockito.times(1))
                .findById(1L);
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return created customer")
    public void createCustomer() throws Exception {
        Customer customer = getCustomer();
        Mockito.when(customerService
                        .save(ArgumentMatchers.any(Customer.class)))
                .thenReturn(getCustomer());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(customerService,
                        Mockito.times(1))
                .save(ArgumentMatchers.any(Customer.class));
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    @Disabled
    @DisplayName("should return updated customer")
    public void updateCustomer() throws Exception {

        Customer customer = getCustomer();
        Mockito.when(customerService.update(ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(customerService,
                        Mockito.times(1))
                .update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Customer.class));
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("should return no content after deleting customer")
    public void deleteCustomer() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(customerService,
                        Mockito.times(1))
                .delete(1L);
        Mockito.verifyNoMoreInteractions(customerService);
    }
}