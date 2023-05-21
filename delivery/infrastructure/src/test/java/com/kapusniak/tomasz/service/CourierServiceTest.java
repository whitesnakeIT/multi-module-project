package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.mapstruct.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.CourierCompany;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
class CourierServiceTest {

    Courier courier = new Courier();
    CourierEntity courierEntity = new CourierEntity();
    List<Courier> courierList = new ArrayList<>();
    List<CourierEntity> courierEntityList = new ArrayList<>();
    @Mock
    private CourierJpaRepository courierRepository;
    @Mock
    private CourierEntityMapper courierEntityMapper;

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

        courierEntity.setId(1L);
        courierEntity.setCourierCompany(CourierCompany.DHL);
        courierEntity.setFirstName("testFirstName");
        courierEntity.setLastName("testLastName");

        courierEntityList.add(courierEntity);
        courierEntityList.add(courierEntity);

        when(courierEntityMapper
                .mapToEntity(any(Courier.class)))
                .thenReturn(courierEntity);
        when(courierEntityMapper
                .mapToApiModel(any(CourierEntity.class)))
                .thenReturn(courier);
    }

    @Test
    @DisplayName("should correctly save an Courier entity exactly once")
    void save() {

        //given
        when(courierRepository
                .save(any(CourierEntity.class)))
                .thenReturn(courierEntity);

        // when
        Courier result = courierService.save(courier);

        // then
        then(courierRepository)
                .should(times(1))
                .save(courierEntityMapper.mapToEntity(courier));

        // verify
        assertThat(courier).isEqualTo(result);
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
                .willReturn(courierEntityList);

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
                .willReturn(Optional.of(courierEntity));
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
                .willReturn(Optional.of(courierEntity));
        Long courierId = 1L;

        // when
        courierService.delete(courierId);

        // then
        then(courierRepository)
                .should(times(1))
                .delete(courierEntity);
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