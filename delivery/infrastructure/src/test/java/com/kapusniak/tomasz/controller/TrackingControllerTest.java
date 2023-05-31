package com.kapusniak.tomasz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.service.TrackingService;
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
public class TrackingControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackingService trackingService;

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
    public void getAllTracking() throws Exception {
        // given
        List<Tracking> trackingList = getTrackingList();
        when(trackingService.findAll()).thenReturn(trackingList);

        // when
        mockMvc.perform(get("/api/v1/tracking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].localization").value("testLocalization1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].localization").value("testLocalization2"));

        // then
        verify(trackingService,
                times(1))
                .findAll();
        verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should correctly return tracking based on tracking id")
    public void getTrackingById() throws Exception {
        // given
        Tracking tracking = getTracking();
        when(trackingService.findById(1L)).thenReturn(tracking);

        // when
        mockMvc.perform(get("/api/v1/tracking/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.localization").value("testLocalization1"));

        // then
        verify(trackingService,
                times(1))
                .findById(1L);
        verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should return created tracking")
    public void createTracking() throws Exception {
        // given
        Tracking tracking = getTracking();
        when(trackingService
                .save(any(Tracking.class)))
                .thenReturn(getTracking());

        // when
        mockMvc.perform(post("/api/v1/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tracking)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.localization").value("testLocalization1"));

        // then
        verify(trackingService,
                times(1))
                .save(any(Tracking.class));
        verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should return updated tracking")
    public void updateTracking() throws Exception {
        // given
        Tracking tracking = getTracking();
        when(trackingService.update(anyLong(),
                any(Tracking.class)))
                .thenReturn(tracking);

        // when
        mockMvc.perform(put("/api/v1/tracking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tracking)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.localization").value("testLocalization1"));

        // then
        verify(trackingService,
                times(1))
                .update(eq(1L), any(Tracking.class));
        verifyNoMoreInteractions(trackingService);
    }

    @Test
    @DisplayName("should return no content after deleting tracking")
    public void deleteTracking() throws Exception {
        // when
        mockMvc.perform(delete("/api/v1/tracking/1"))
                .andExpect(status().isNoContent());

        // then
        verify(trackingService,
                times(1))
                .delete(1L);
        verifyNoMoreInteractions(trackingService);
    }
}