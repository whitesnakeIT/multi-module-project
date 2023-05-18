package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.enums.DeliveryStatus;
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
class DeliveryServiceTest {

    DeliveryEntity delivery = new DeliveryEntity();

    List<DeliveryEntity> deliveryList = new ArrayList<>();

    @Mock
    private DeliveryJpaRepository deliveryRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setup() {
        delivery.setId(1L);
        delivery.setDeliveryTime(LocalDateTime
                .of(2023, 5, 3,
                        12, 0, 0));
        delivery.setDeliveryStatus(DeliveryStatus.CREATED);
        delivery.setPrice(BigDecimal.valueOf(50.00));

        deliveryList.add(delivery);
        deliveryList.add(delivery);
    }

    @Test
    @DisplayName("should correctly save an Delivery entity exactly once")
    void save() {
        // when
        deliveryService.save(delivery);

        // then
        then(deliveryRepository)
                .should(times(1))
                .save(delivery);
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
                .willReturn(deliveryList);

        // when
        List<DeliveryEntity> allDeliverys = deliveryService.findAll();

        // then
        assertThat(allDeliverys.size())
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
                .willReturn(Optional.of(delivery));
        Long deliveryId = 1L;

        // when
        DeliveryEntity deliveryById = deliveryService.findById(deliveryId);

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
                .willReturn(Optional.of(delivery));
        Long deliveryId = 1L;

        // when
        deliveryService.delete(deliveryId);

        // then
        then(deliveryRepository)
                .should(times(1))
                .delete(delivery);
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