package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.service.CourierService;
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
public class CourierControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourierService courierService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CourierController courierController = new CourierController(courierService);
        mockMvc = MockMvcBuilders.standaloneSetup(courierController).build();
    }

    public Courier getCourier() {
        Courier courier1 = new Courier();
        courier1.setId(1L);
        courier1.setFirstName("John");
        return courier1;
    }

    public List<Courier> getCourierList() {
        List<Courier> couriers = new ArrayList<>();
        Courier courier1 = new Courier();
        courier1.setId(1L);
        courier1.setFirstName("John");
        Courier courier2 = new Courier();
        courier2.setId(2L);
        courier2.setFirstName("Tom");
        couriers.add(courier1);
        couriers.add(courier2);
        return couriers;
    }

    @Test
    @DisplayName("should correctly return list of couriers")
    public void getAllCouriers() throws Exception {
        List<Courier> couriers = getCourierList();
        Mockito.when(courierService.findAll()).thenReturn(couriers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/couriers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Tom"));

        Mockito.verify(courierService,
                        Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should correctly return courier based on courier id")
    public void getCourierById() throws Exception {
        Courier courier = getCourier();

        Mockito.when(courierService.findById(1L)).thenReturn(courier);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/couriers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(courierService,
                        Mockito.times(1))
                .findById(1L);
        Mockito.verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should return created courier")
    public void createCourier() throws Exception {
        Courier courier = getCourier();
        Mockito.when(courierService
                        .save(ArgumentMatchers.any(Courier.class)))
                .thenReturn(getCourier());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(courierService,
                        Mockito.times(1))
                .save(ArgumentMatchers.any(Courier.class));
        Mockito.verifyNoMoreInteractions(courierService);
    }

    @Test
    @Disabled
    @DisplayName("should return updated courier")
    public void updateCourier() throws Exception {

        Courier courier = getCourier();
        Mockito.when(courierService.update(ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(Courier.class))).thenReturn(courier);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        Mockito.verify(courierService,
                        Mockito.times(1))
                .update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Courier.class));
        Mockito.verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should return no content after deleting courier")
    public void deleteCourier() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/couriers/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(courierService,
                        Mockito.times(1))
                .delete(1L);
        Mockito.verifyNoMoreInteractions(courierService);
    }
}