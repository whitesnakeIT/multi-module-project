package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.service.DeliveryService;
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
        Mockito.when(deliveryService.findAll()).thenReturn(deliveries);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deliveries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].deliveryStatus").value(DeliveryStatus.CREATED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].deliveryStatus").value(DeliveryStatus.DELIVERED.toString()));

        Mockito.verify(deliveryService,
                        Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should correctly return delivery based on delivery id")
    public void getDeliveryById() throws Exception {
        Delivery delivery = getDelivery();

        Mockito.when(deliveryService.findById(1L)).thenReturn(delivery);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deliveries/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        Mockito.verify(deliveryService,
                        Mockito.times(1))
                .findById(1L);
        Mockito.verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should return created delivery")
    public void createDelivery() throws Exception {
        Delivery delivery = getDelivery();
        Mockito.when(deliveryService
                        .save(ArgumentMatchers.any(Delivery.class)))
                .thenReturn(getDelivery());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        Mockito.verify(deliveryService,
                        Mockito.times(1))
                .save(ArgumentMatchers.any(Delivery.class));
        Mockito.verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @Disabled
    @DisplayName("should return updated delivery")
    public void updateDelivery() throws Exception {

        Delivery delivery = getDelivery();
        Mockito.when(deliveryService.update(ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(Delivery.class))).thenReturn(delivery);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/deliveries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryStatus").value(DeliveryStatus.CREATED.toString()));

        Mockito.verify(deliveryService,
                        Mockito.times(1))
                .update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Delivery.class));
        Mockito.verifyNoMoreInteractions(deliveryService);
    }

    @Test
    @DisplayName("should return no content after deleting delivery")
    public void deleteDelivery() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/deliveries/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deliveryService,
                        Mockito.times(1))
                .delete(1L);
        Mockito.verifyNoMoreInteractions(deliveryService);
    }
}