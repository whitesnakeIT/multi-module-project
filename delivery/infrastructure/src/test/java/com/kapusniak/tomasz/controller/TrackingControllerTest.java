package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.service.TrackingService;
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
public class TrackingControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrackingService trackingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TrackingController trackingController = new TrackingController(trackingService);
        mockMvc = MockMvcBuilders.standaloneSetup(trackingController).build();
    }

    public Tracking getTracking() {
        Tracking tracking1 = new Tracking();
        tracking1.setId(1L);
        tracking1.setLocalization("testLocalization1");
        return tracking1;
    }

    public List<Tracking> getTrackingList() {
        List<Tracking> trackingList = new ArrayList<>();
        Tracking tracking1 = new Tracking();
        tracking1.setId(1L);
        tracking1.setLocalization("testLocalization1");
        Tracking tracking2 = new Tracking();
        tracking2.setId(2L);
        tracking2.setLocalization("testLocalization2");
        trackingList.add(tracking1);
        trackingList.add(tracking2);
        return trackingList;
    }

    @Test
    @DisplayName("should correctly return list of tracking")
    public void getAllTrackings() throws Exception {
        List<Tracking> trackingList = getTrackingList();
        Mockito.when(trackingService.findAll()).thenReturn(trackingList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tracking"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].localization").value("testLocalization1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].localization").value("testLocalization2"));

        Mockito.verify(trackingService,
                        Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should correctly return tracking based on tracking id")
    public void getTrackingById() throws Exception {
        Tracking tracking = getTracking();

        Mockito.when(trackingService.findById(1L)).thenReturn(tracking);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tracking/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.localization").value("testLocalization1"));

        Mockito.verify(trackingService,
                        Mockito.times(1))
                .findById(1L);
        Mockito.verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should return created tracking")
    public void createTracking() throws Exception {
        Tracking tracking = getTracking();
        Mockito.when(trackingService
                        .save(ArgumentMatchers.any(Tracking.class)))
                .thenReturn(getTracking());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tracking)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.localization").value("testLocalization1"));

        Mockito.verify(trackingService,
                        Mockito.times(1))
                .save(ArgumentMatchers.any(Tracking.class));
        Mockito.verifyNoMoreInteractions(trackingService);
    }

    @Test
    @Disabled
    @DisplayName("should return updated tracking")
    public void updateTracking() throws Exception {

        Tracking tracking = getTracking();
        Mockito.when(trackingService.update(ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(Tracking.class))).thenReturn(tracking);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tracking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tracking)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.localization").value("testLocalization1"));

        Mockito.verify(trackingService,
                        Mockito.times(1))
                .update(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Tracking.class));
        Mockito.verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should return no content after deleting tracking")
    public void deleteTracking() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tracking/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(trackingService,
                        Mockito.times(1))
                .delete(1L);
        Mockito.verifyNoMoreInteractions(trackingService);
    }
}