package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.entity.Delivery;
import com.kapusniak.tomasz.enums.DeliveryStatus;
import com.kapusniak.tomasz.service.DeliveryService;
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
public class DeliveryControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeliveryService deliveryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DeliveryController deliveryController = new DeliveryController(deliveryService);
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
    }

    public Delivery getDelivery() {
        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        delivery1.setDeliveryStatus(DeliveryStatus.CREATED);
        return delivery1;
    }

    public List<Delivery> getDeliveryList() {
        List<Delivery> deliveries = new ArrayList<>();
        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        delivery1.setDeliveryStatus(DeliveryStatus.CREATED);
        Delivery delivery2 = new Delivery();
        delivery2.setId(2L);
        delivery2.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveries.add(delivery1);
        deliveries.add(delivery2);
        return deliveries;
    }

    @Test
    @DisplayName("should correctly return list of deliveries")
    public void getAllDeliveries() throws Exception {
        List<Delivery> deliveries = getDeliveryList();
        when(deliveryService.findAll()).thenReturn(deliveries);

        mockMvc.perform(get("/api/v1/deliveries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].deliveryStatus").value(DeliveryStatus.CREATED.toString()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].deliveryStatus").value(DeliveryStatus.DELIVERED.toString()));

        verify(deliveryService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should correctly return delivery based on delivery id")
    public void getDeliveryById() throws Exception {
        Delivery delivery = getDelivery();

        when(deliveryService.findById(1L)).thenReturn(delivery);

        mockMvc.perform(get("/api/v1/deliveries/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        verify(deliveryService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should return created delivery")
    public void createDelivery() throws Exception {
        Delivery delivery = getDelivery();
        when(deliveryService
                .save(any(Delivery.class)))
                .thenReturn(getDelivery());

        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        verify(deliveryService,
                times(1))
                .save(any(Delivery.class));
        verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should return updated delivery")
    public void updateDelivery() throws Exception {

        Delivery delivery = getDelivery();
        when(deliveryService.update(eq(1L),
                any(Delivery.class))).thenReturn(delivery);
        mockMvc.perform(put("/api/v1/deliveries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        verify(deliveryService,
                times(1))
                .update(eq(1L), any(Delivery.class));
        verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should return no content after deleting delivery")
    public void deleteDelivery() throws Exception {

        mockMvc.perform(delete("/api/v1/deliveries/1"))
                .andExpect(status().isNoContent());

        verify(deliveryService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(deliveryService);
    }
}