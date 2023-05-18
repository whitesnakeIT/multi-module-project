package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.service.CourierService;
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

    public CourierEntity getCourier() {
        CourierEntity courier1 = new CourierEntity();
        courier1.setId(1L);
        courier1.setFirstName("John");
        return courier1;
    }

    public List<CourierEntity> getCourierList() {
        List<CourierEntity> couriers = new ArrayList<>();
        CourierEntity courier1 = new CourierEntity();
        courier1.setId(1L);
        courier1.setFirstName("John");
        CourierEntity courier2 = new CourierEntity();
        courier2.setId(2L);
        courier2.setFirstName("Tom");
        couriers.add(courier1);
        couriers.add(courier2);
        return couriers;
    }

    @Test
    @DisplayName("should correctly return list of couriers")
    public void getAllCouriers() throws Exception {
        List<CourierEntity> couriers = getCourierList();
        when(courierService.findAll()).thenReturn(couriers);

        mockMvc.perform(get("/api/v1/couriers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Tom"));

        verify(courierService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should correctly return courier based on courier id")
    public void getCourierById() throws Exception {
        CourierEntity courier = getCourier();

        when(courierService.findById(1L)).thenReturn(courier);

        mockMvc.perform(get("/api/v1/couriers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(courierService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should return created courier")
    public void createCourier() throws Exception {
        CourierEntity courier = getCourier();
        when(courierService
                .save(any(CourierEntity.class)))
                .thenReturn(getCourier());

        mockMvc.perform(post("/api/v1/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(courierService,
                times(1))
                .save(any(CourierEntity.class));
        verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should return updated courier")
    public void updateCourier() throws Exception {

        CourierEntity courier = getCourier();
        when(courierService.update(eq(1L),
                any(CourierEntity.class))).thenReturn(courier);
        mockMvc.perform(put("/api/v1/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(courierService,
                times(1))
                .update(eq(1L), any(CourierEntity.class));
        verifyNoMoreInteractions(courierService);
    }

    @Test
    @DisplayName("should return no content after deleting courier")
    public void deleteCourier() throws Exception {

        mockMvc.perform(delete("/api/v1/couriers/1"))
                .andExpect(status().isNoContent());

        verify(courierService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(courierService);
    }
}