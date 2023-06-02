package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.service.TrackingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@TestPropertySource("classpath:application-integration-test.properties")
@SqlGroup(
        @Sql(
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:integration-test-scripts/cleanup.sql",
                        "classpath:integration-test-scripts/insert-data.sql"})
)
public class TrackingTest {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should correctly get Tracking from database and verify" +
            " properties with Tracking from controller method")
    void getTrackingExisting() throws Exception {
        // given
        Long trackingId = 1L;
        Tracking tracking = trackingService.findById(trackingId);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/tracking/" + trackingId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(tracking.getId()));
    }

    @Test
    @DisplayName("should throw an exception when provided tracking id is not existing" +
            " in database for searching")
    void getTrackingNonExisting() {
        // given
        Long trackingId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/tracking/" + trackingId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should correctly return list Tracking from database after executing" +
            " method from controller")
    void getAllTracking() throws Exception {
        // given
        List<Tracking> trackingList = trackingService.findAll();

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/tracking"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id")
                        .value(trackingList.get(0).getId()))
                .andExpect(jsonPath("$[1].id")
                        .value(trackingList.get(1).getId()));


    }

    @Sql("classpath:integration-test-scripts/cleanup.sql")
    @Test
    @DisplayName("should correctly return empty list of Tracking when table is empty" +
            "after executing method from controller")
    void getAllTrackingEmpty() throws Exception {
        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/tracking"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("should save Tracking to database after executing method" +
            " from controller")
    void createTracking() throws Exception {
        // given
        Tracking tracking = new Tracking();
        tracking.setLocalization("localization");

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tracking)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.localization").value(tracking.getLocalization()));
    }

    @Test
    @DisplayName("should delete Tracking from database after executing" +
            " method from controller")
    void deleteTrackingExisting() throws Exception {
        // given
        Long trackingId = 1L;
        int sizeBeforeDeleting = trackingService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/tracking/" + trackingId));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = trackingService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided tracking id is not existing" +
            " in database for deleting")
    void deleteTrackingNonExisting() {
        // given
        Long trackingId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/tracking/" + trackingId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should save edited Tracking to database after executing method" +
            " from controller")
    void updateTracking() throws Exception {
        // given
        Long trackingId = 1L;
        Tracking trackingBeforeEdit = trackingService.findById(trackingId);
        String newLocalization = "newLocalization";

        // and
        trackingBeforeEdit.setLocalization(newLocalization);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/tracking/" + trackingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackingBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(trackingBeforeEdit.getId()))
                .andExpect(jsonPath("$.localization").value(newLocalization));

        // and
        Tracking trackingAfterEdit = trackingService.findById(trackingId);
        assertThat(trackingAfterEdit.getLocalization()).isEqualTo(newLocalization);
    }
}