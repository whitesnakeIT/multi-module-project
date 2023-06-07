package com.kapusniak.tomasz.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.CourierCompany;
import com.kapusniak.tomasz.service.CourierService;
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
import java.util.UUID;

import static com.kapusniak.tomasz.openapi.model.CourierCompany.FEDEX;
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
public class CourierTest {

    private static final UUID UUID_COURIER_1 = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");

    @Autowired
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private Courier prepareCourier() {
        Courier courier = new Courier();
        UUID courierUuid = UUID_COURIER_1;
        String firstName = "testFirstName";
        String lastName = "testLastName";
        CourierCompany courierCompany = FEDEX;

        courier.setUuid(courierUuid);
        courier.setFirstName(firstName);
        courier.setLastName(lastName);

        courier.setCourierCompany(courierCompany);

        return courier;
    }

    @Test
    @DisplayName("should correctly get Courier from database and verify" +
            " properties with Courier from controller method")
    void getCourierExisting() throws Exception {
        // given
        UUID courierUuid = UUID_COURIER_1;
        Courier courier = courierService.findByUuid(courierUuid);

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/couriers/" + courierUuid));


        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(courier.getUuid().toString()));
    }

    @Test
    @DisplayName("should throw an exception when provided courier uuid is not existing" +
            " in database for searching")
    void getCourierNonExisting() {
        // given
        UUID courierUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/couriers/" + courierUuid)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should correctly return list Couriers from database after executing" +
            " method from controller")
    void getAllCouriers() throws Exception {
        // given
        List<Courier> courierList = courierService.findAll();

        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/couriers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uuid")
                        .value(courierList.get(0).getUuid().toString()))
                .andExpect(jsonPath("$[1].uuid")
                        .value(courierList.get(1).getUuid().toString()));


    }

    @Sql("classpath:integration-test-scripts/cleanup.sql")
    @Test
    @DisplayName("should correctly return empty list of Couriers when table is empty" +
            "after executing method from controller")
    void getAllCouriersEmpty() throws Exception {
        // when
        ResultActions result =
                mockMvc.perform(get(
                        "/api/v1/couriers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DisplayName("should save edited Courier to database after executing method" +
            " from controller")
    void createCourier() throws Exception {
        // given
        Courier courier = prepareCourier();

        // when
        ResultActions result = mockMvc
                .perform(post("/api/v1/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value(courier.getFirstName()));
    }

    @Test
    @DisplayName("should delete Courier from database after executing" +
            " method from controller")
    void deleteCourierExisting() throws Exception {
        // given
        UUID courierUuid = UUID_COURIER_1;
        int sizeBeforeDeleting = courierService.findAll().size();

        // when
        ResultActions result =
                mockMvc.perform(delete(
                        "/api/v1/couriers/" + courierUuid));

        // then
        result.andExpect(status().isNoContent());

        // and
        int sizeAfterDeleting = courierService.findAll().size();
        assertThat(sizeAfterDeleting).isEqualTo(sizeBeforeDeleting - 1);

    }

    @Test
    @DisplayName("should throw an exception when provided courier uuid is not existing" +
            " in database for deleting")
    void deleteCourierNonExisting() {
        // given
        UUID courierUuid = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(
                () -> mockMvc.perform(get(
                        "/api/v1/couriers/" + courierUuid)
                )
        );

        // then
        assertThat(throwable.getCause())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should save Courier to database after executing method" +
            " from controller")
    void updateCourier() throws Exception {
        // given
        UUID courierUuid = UUID_COURIER_1;
        Courier courierBeforeEdit = courierService.findByUuid(courierUuid);
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";

        // and
        courierBeforeEdit.setFirstName(newFirstName);
        courierBeforeEdit.setLastName(newLastName);

        // when
        ResultActions result = mockMvc
                .perform(put("/api/v1/couriers/" + courierUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courierBeforeEdit)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(courierBeforeEdit.getUuid().toString()))
                .andExpect(jsonPath("$.firstName").value(newFirstName))
                .andExpect(jsonPath("$.lastName").value(newLastName));

        // and
        Courier courierAfterEdit = courierService.findByUuid(courierUuid);
        assertThat(courierAfterEdit.getFirstName()).isEqualTo(newFirstName);
        assertThat(courierAfterEdit.getLastName()).isEqualTo(newLastName);
    }
}