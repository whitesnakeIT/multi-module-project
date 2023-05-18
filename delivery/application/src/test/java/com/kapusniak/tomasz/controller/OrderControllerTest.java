package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.service.OrderService;
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
public class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    public OrderEntity getOrder() {
        OrderEntity order1 = new OrderEntity();
        order1.setId(1L);
        order1.setSenderAddress("testSenderAddress1");
        return order1;
    }

    public List<OrderEntity> getOrderList() {
        List<OrderEntity> orders = new ArrayList<>();
        OrderEntity order1 = new OrderEntity();
        order1.setId(1L);
        order1.setSenderAddress("testSenderAddress1");
        OrderEntity order2 = new OrderEntity();
        order2.setId(2L);
        order2.setSenderAddress("testSenderAddress2");
        orders.add(order1);
        orders.add(order2);
        return orders;
    }

    @Test
    @DisplayName("should correctly return list of orders")
    public void getAllOrders() throws Exception {
        List<OrderEntity> orders = getOrderList();
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].senderAddress").value("testSenderAddress1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].senderAddress").value("testSenderAddress2"));

        verify(orderService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should correctly return order based on order id")
    public void getOrderById() throws Exception {
        OrderEntity order = getOrder();

        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        verify(orderService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return created order")
    public void createOrder() throws Exception {
        OrderEntity order = getOrder();
        when(orderService
                .save(any(OrderEntity.class)))
                .thenReturn(getOrder());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        verify(orderService,
                times(1))
                .save(any(OrderEntity.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return updated order")
    public void updateOrder() throws Exception {

        OrderEntity order = getOrder();
        when(orderService.update(eq(1L),
                any(OrderEntity.class))).thenReturn(order);
        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        verify(orderService,
                times(1))
                .update(eq(1L), any(OrderEntity.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return no content after deleting order")
    public void deleteOrder() throws Exception {

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(orderService);
    }
}