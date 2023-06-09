package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.openapi.model.Order;
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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.openapi.model.DeliveryStatus.IN_TRANSIT;
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

    private static final UUID DELIVERY_UUID_1 = UUID.fromString("31822712-94b3-43ed-9aac-24613948ca79");

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Delivery prepareDelivery() {
        Delivery delivery = new Delivery();
        UUID deliveryUuid = DELIVERY_UUID_1;
        Double price = 40.50D;
        LocalDateTime deliveryLocalDateTime = LocalDateTime.of(2023, 6, 5, 23, 30, 0);
        OffsetDateTime deliveryOffsetDateTime = deliveryLocalDateTime.atOffset(ZoneOffset.UTC);
        DeliveryStatus deliveryStatus = IN_TRANSIT;
        UUID orderUuid = UUID.fromString("06a4084b-5837-4303-ab5a-8b50fedb3898");
        UUID courierUuid = UUID.fromString("1f263424-a92a-49a6-b38f-eaa2861ab332");

        delivery.setUuid(deliveryUuid);
        delivery.setPrice(price);
        delivery.setDeliveryTime(deliveryOffsetDateTime);
        delivery.setDeliveryStatus(deliveryStatus);
        delivery.setUuid(deliveryUuid);

        Order newOrder = new Order();
        Long orderId = 1L;
        newOrder.setId(orderId);
        newOrder.setUuid(orderUuid);

        Courier newCourier = new Courier();
        Long courierId = 3L;
        newCourier.setId(courierId);
        newCourier.setUuid(courierUuid);


        return delivery;
    }

    @Test
    @DisplayName("should correctly get Delivery from database and verify" +
            " properties with Delivery from controller method")
    void getDeliveryExisting() throws Exception {
        // given
        UUID deliveryUuid = DELIVERY_UUID_1;
        Delivery delivery = deliveryService.findByUuid(deliveryUuid);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/deliveries/" + deliveryUuid));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(delivery.getUuid().toString()));
    }

    @Test
    @DisplayName("should throw an exception when provided delivery uuid is not existing" +
            " in database for searching")
    void getDeliveryNonExisting() {
        // given
        UUID deliveryUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/deliveries/" + deliveryUuid)
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
                .andExpect(jsonPath("$[0].uuid")
                        .value(deliveryList.get(0).getUuid().toString()))
                .andExpect(jsonPath("$[1].uuid")
                        .value(deliveryList.get(1).getUuid().toString()));
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
        Delivery delivery = prepareDelivery();


        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delivery)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.deliveryStatus").value(delivery.getDeliveryStatus().toString()));
    }

    @Test
    @DisplayName("should delete Delivery from database after executing" +
            " method from controller")
    void deleteDeliveryExisting() throws Exception {
        // given
        UUID deliveryUuid = DELIVERY_UUID_1;
        int sizeBeforeDeleting = deliveryService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/deliveries/" + deliveryUuid));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = deliveryService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided delivery uuid is not existing" +
            " in database for deleting")
    void deleteDeliveryNonExisting() {
        // given
        UUID deliveryUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/deliveries/" + deliveryUuid)
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
        UUID deliveryUuid = DELIVERY_UUID_1;
        Delivery deliveryBeforeEdit = deliveryService.findByUuid(deliveryUuid);
        DeliveryStatus newDeliveryStatus = DeliveryStatus.FAILED_DELIVERY_ATTEMPT;

        // and
        deliveryBeforeEdit.setDeliveryStatus(newDeliveryStatus);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/deliveries/" + deliveryUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(deliveryBeforeEdit.getUuid().toString()))
                .andExpect(jsonPath("$.deliveryStatus").value(newDeliveryStatus.toString()));

        // and
        Delivery deliveryAfterEdit = deliveryService.findByUuid(deliveryUuid);
        assertThat(deliveryAfterEdit.getDeliveryStatus()).isEqualTo(newDeliveryStatus);
    }
}