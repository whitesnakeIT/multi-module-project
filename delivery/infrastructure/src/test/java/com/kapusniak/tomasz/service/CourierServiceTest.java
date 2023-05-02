package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Courier;
import com.kapusniak.tomasz.enums.CourierCompany;
import com.kapusniak.tomasz.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
class CourierServiceTest {

    Courier courier = new Courier();

    List<Courier> courierList = new ArrayList<>();

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    void setup() {
        courier.setId(1L);
        courier.setCourierCompany(CourierCompany.DHL);
        courier.setFirstName("testFirstName");
        courier.setLastName("testLastName");

        courierList.add(courier);
        courierList.add(courier);
    }

    @Test
    @DisplayName("should correctly save an Courier entity exactly once")
    void save() {
        // when
        courierService.save(courier);

        // then
        then(courierRepository)
                .should(times(1))
                .save(courier);
    }

    @Test
    @DisplayName("should throw an exception when Courier entity is null and check method invocations")
    void saveNull() {

        // given
        courier = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.save(courier));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Saving courier failed. Courier is null.");

        // verify
        then(courierRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of all courier with correct size and check method invocations")
    void findAll() {

        // given
        given(courierRepository.findAll())
                .willReturn(courierList);

        // when
        List<Courier> allCouriers = courierService.findAll();

        // then
        assertThat(allCouriers.size())
                .isEqualTo(2);

        // verify
        then(courierRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("should return courier based on courier id")
    void findById() {

        // given
        given(courierRepository.findById(
                anyLong()))
                .willReturn(Optional.of(courier));
        Long courierId = 1L;

        // when
        Courier courierById = courierService.findById(courierId);

        // then
        assertThat(courierById.getId())
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when courier id is null")
    void findByIdNull() {

        // given
        Long courierId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.findById(courierId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for courier failed. Courier id is null.");

        // verify
        then(courierRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should delete an courier based on courier id")
    void delete() {

        // given
        given(courierRepository.findById(
                anyLong()))
                .willReturn(Optional.of(courier));
        Long courierId = 1L;

        // when
        courierService.delete(courierId);

        // then
        then(courierRepository)
                .should(times(1))
                .delete(courier);
    }

    @Test
    @DisplayName("should throw an exception when courier id is null")
    void deleteNull() {
        // given
        Long courierId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.delete(courierId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting courier failed. Courier id is null.");
    }
}