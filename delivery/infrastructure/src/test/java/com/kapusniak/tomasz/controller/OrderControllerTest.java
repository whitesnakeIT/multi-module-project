package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Order;
import com.kapusniak.tomasz.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    public Order getOrder() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setSenderAddress("testSenderAddress1");

        return order1;
    }

    public List<Order> getOrderList() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setSenderAddress("testSenderAddress1");
        Order order2 = new Order();
        order2.setId(2L);
        order2.setSenderAddress("testSenderAddress2");
        orders.add(order1);
        orders.add(order2);

        return orders;
    }

    @Test
    @DisplayName("should correctly return list of orders")
    public void getAllOrders() throws Exception {
        // given
        List<Order> orders = getOrderList();
        when(orderService.findAll()).thenReturn(orders);

        // when
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].senderAddress").value("testSenderAddress1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].senderAddress").value("testSenderAddress2"));

        // then
        verify(orderService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should correctly return order based on order id")
    public void getOrderById() throws Exception {
        // given
        Order order = getOrder();
        when(orderService.findById(1L)).thenReturn(order);

        // when
        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        // then
        verify(orderService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return created order")
    public void createOrder() throws Exception {
        // given
        Order order = getOrder();
        when(orderService
                .save(any(Order.class)))
                .thenReturn(getOrder());

        // when
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        // then
        verify(orderService,
                times(1))
                .save(any(Order.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return updated order")
    public void updateOrder() throws Exception {
        // given
        Order order = getOrder();
        when(orderService.update(anyLong(),
                any(Order.class)))
                .thenReturn(order);

        // when
        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAddress").value("testSenderAddress1"));

        // then
        verify(orderService,
                times(1))
                .update(eq(1L), any(Order.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("should return no content after deleting order")
    public void deleteOrder() throws Exception {
        // when
        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        // then
        verify(orderService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(orderService);
    }
}