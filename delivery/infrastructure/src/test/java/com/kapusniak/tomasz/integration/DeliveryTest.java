package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.service.DeliveryService;
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
public class DeliveryTest {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should correctly get Delivery from database and verify" +
            " properties with Delivery from controller method")
    void getDeliveryExisting() throws Exception {
        // given
        Long deliveryId = 1L;
        Delivery delivery = deliveryService.findById(deliveryId);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/deliveries/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(delivery.getId()));
    }

    @Test
    @DisplayName("should throw an exception when provided delivery id is not existing" +
            " in database for searching")
    void getDeliveryNonExisting() {
        // given
        Long deliveryId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/deliveries/" + deliveryId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should correctly return list Deliveries from database after executing" +
            " method from controller")
    void getAllDeliveries() throws Exception {
        // given
        List<Delivery> deliveryList = deliveryService.findAll();

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/deliveries"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id")
                        .value(deliveryList.get(0).getId()))
                .andExpect(jsonPath("$[1].id")
                        .value(deliveryList.get(1).getId()));


    }

    @Sql("classpath:integration-test-scripts/cleanup.sql")
    @Test
    @DisplayName("should correctly return empty list of Deliveries when table is empty" +
            "after executing method from controller")
    void getAllDeliveriesEmpty() throws Exception {
        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/deliveries"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("should save edited Delivery to database after executing method" +
            " from controller")
    void createDelivery() throws Exception {
        // given
        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.IN_TRANSIT);

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.deliveryStatus").value(delivery.getDeliveryStatus().toString()));
    }

    @Test
    @DisplayName("should delete Delivery from database after executing" +
            " method from controller")
    void deleteDeliveryExisting() throws Exception {
        // given
        Long deliveryId = 1L;
        int sizeBeforeDeleting = deliveryService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/deliveries/" + deliveryId));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = deliveryService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided delivery id is not existing" +
            " in database for deleting")
    void deleteDeliveryNonExisting() {
        // given
        Long deliveryId = 3L;

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/deliveries/" + deliveryId)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should save Delivery to database after executing method" +
            " from controller")
    void updateDelivery() throws Exception {
        // given
        Long deliveryId = 1L;
        Delivery deliveryBeforeEdit = deliveryService.findById(deliveryId);
        DeliveryStatus newDeliveryStatus = DeliveryStatus.FAILED_DELIVERY_ATTEMPT;

        // and
        deliveryBeforeEdit.setDeliveryStatus(newDeliveryStatus);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/deliveries/" + deliveryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deliveryBeforeEdit.getId()))
                .andExpect(jsonPath("$.deliveryStatus").value(newDeliveryStatus.toString()));

        // and
        Delivery deliveryAfterEdit = deliveryService.findById(deliveryId);
        assertThat(deliveryAfterEdit.getDeliveryStatus()).isEqualTo(newDeliveryStatus);
    }
}