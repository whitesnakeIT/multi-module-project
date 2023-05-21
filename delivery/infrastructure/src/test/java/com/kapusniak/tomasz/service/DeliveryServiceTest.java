package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.mapstruct.DeliveryEntityMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class DeliveryServiceTest {

    Delivery delivery = new Delivery();
    DeliveryEntity deliveryEntity = new DeliveryEntity();

    List<Delivery> deliveryList = new ArrayList<>();
    List<DeliveryEntity> deliveryEntityList = new ArrayList<>();

    @Mock
    private DeliveryJpaRepository deliveryRepository;

    @Mock
    private DeliveryEntityMapper deliveryEntityMapper;
    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setup() {
        delivery.setId(1L);
        delivery.setDeliveryTime(LocalDateTime
                .of(2023, 5, 3,
                        12, 0, 0).atOffset(ZoneOffset.UTC));
        delivery.setDeliveryStatus(DeliveryStatus.CREATED);
        delivery.setPrice(50.00);

        deliveryList.add(delivery);
        deliveryList.add(delivery);

        deliveryEntity.setId(1L);
        deliveryEntity.setDeliveryTime(LocalDateTime
                .of(2023, 5, 3,
                        12, 0, 0));
        deliveryEntity.setDeliveryStatus(DeliveryStatus.CREATED);
        deliveryEntity.setPrice(BigDecimal.valueOf(50.00));

        deliveryEntityList.add(deliveryEntity);
        deliveryEntityList.add(deliveryEntity);

        when(deliveryEntityMapper
                .mapToEntity(any(Delivery.class)))
                .thenReturn(deliveryEntity);
        when(deliveryEntityMapper
                .mapToApiModel(any(DeliveryEntity.class)))
                .thenReturn(delivery);
    }

    @Test
    @DisplayName("should correctly save an Delivery entity exactly once")
    void save() {

        //given
        when(deliveryRepository
                .save(any(DeliveryEntity.class)))
                .thenReturn(deliveryEntity);

        // when
        Delivery result = deliveryService.save(delivery);

        // then
        then(deliveryRepository)
                .should(times(1))
                .save(deliveryEntityMapper.mapToEntity(delivery));

        // verify
        assertThat(delivery).isEqualTo(result);

    }

    @Test
    @DisplayName("should throw an exception when Delivery entity is null and check method invocations")
    void saveNull() {

        // given
        delivery = null;

        // when
        Throwable throwable = catchThrowable(() ->
                deliveryService.save(delivery));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Saving delivery failed. Delivery is null.");

        // verify
        then(deliveryRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of all delivery with correct size and check method invocations")
    void findAll() {

        // given
        given(deliveryRepository.findAll())
                .willReturn(deliveryEntityList);

        // when
        List<Delivery> allDelivery = deliveryService.findAll();

        // then
        assertThat(allDelivery.size())
                .isEqualTo(2);

        // verify
        then(deliveryRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("should return delivery based on delivery id")
    void findById() {

        // given
        given(deliveryRepository.findById(
                anyLong()))
                .willReturn(Optional.of(deliveryEntity));
        Long deliveryId = 1L;

        // when
        Delivery deliveryById = deliveryService.findById(deliveryId);

        // then
        assertThat(deliveryById.getId())
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when delivery id is null")
    void findByIdNull() {

        // given
        Long deliveryId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                deliveryService.findById(deliveryId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for delivery failed. Delivery id is null.");

        // verify
        then(deliveryRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should delete an delivery based on delivery id")
    void delete() {

        // given
        given(deliveryRepository.findById(
                anyLong()))
                .willReturn(Optional.of(deliveryEntity));
        Long deliveryId = 1L;

        // when
        deliveryService.delete(deliveryId);

        // then
        then(deliveryRepository)
                .should(times(1))
                .delete(deliveryEntity);
    }

    @Test
    @DisplayName("should throw an exception when delivery id is null")
    void deleteNull() {
        // given
        Long deliveryId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                deliveryService.delete(deliveryId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting delivery failed. Delivery id is null.");
    }
}