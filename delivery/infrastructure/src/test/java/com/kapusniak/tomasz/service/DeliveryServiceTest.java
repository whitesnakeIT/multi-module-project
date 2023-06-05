package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.DeliveryEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.openapi.model.Order;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kapusniak.tomasz.openapi.model.DeliveryStatus.CREATED;
import static com.kapusniak.tomasz.openapi.model.DeliveryStatus.IN_TRANSIT;
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
        delivery.setDeliveryStatus(CREATED);
        delivery.setPrice(50.00);

        deliveryList.add(delivery);
        deliveryList.add(delivery);

        deliveryEntity.setId(1L);
        deliveryEntity.setDeliveryTime(LocalDateTime
                .of(2023, 5, 3,
                        12, 0, 0));
        deliveryEntity.setDeliveryStatus(CREATED);
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

    @Test
    @DisplayName("should throw an exception when id is null")
    void updateNullId() {
        // given
        Long deliveryId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                deliveryService.update(deliveryId, delivery));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating delivery failed. Delivery id is null.");
    }

    @Test
    @DisplayName("should throw an exception when delivery is null")
    void updateNullDelivery() {
        // given
        Long deliveryId = 1L;
        Delivery delivery = null;

        // when
        Throwable thrown = catchThrowable(() ->
                deliveryService.update(deliveryId, delivery));

        // then
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating delivery failed. Delivery is null.");
    }

    @Test
    @DisplayName("should throw an exception when newDelivery's id doesn't match deliveryFromDb's id")
    void updateIdMissMatch() {
        // given
        Delivery newDelivery = new Delivery();
        Long oldId = 1L;
        Long newId = 2L;
        newDelivery.setId(newId);

        // and
        when(deliveryRepository.findById(anyLong()))
                .thenReturn(Optional.of(deliveryEntity));
        // when
        Throwable throwable = catchThrowable(() ->
                deliveryService.update(oldId, newDelivery));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating delivery fields failed. Different id's");
    }

    @Test
    @DisplayName("should correctly update delivery when valid id and delivery are provided")
    void shouldUpdateDelivery() {
        // given
        Long deliveryId = 1L;

        // and
        Delivery changedDelivery = prepareDeliveryForEdit();
        DeliveryEntity changedDeliveryEntity = prepareDeliveryEntityForEdit();

        // and
        when(deliveryRepository.findById(anyLong()))
                .thenReturn(Optional.of(deliveryEntity));
        when(deliveryRepository.save(any(DeliveryEntity.class)))
                .thenReturn(changedDeliveryEntity);

        //and
        when(deliveryEntityMapper
                .mapToApiModel(changedDeliveryEntity))
                .thenReturn(changedDelivery);

        // when
        Delivery updatedDelivery = deliveryService.update(deliveryId, changedDelivery);

        // then
        assertThat(updatedDelivery).isNotNull();
        assertThat(updatedDelivery.getId()).isEqualTo(changedDelivery.getId());
        assertThat(updatedDelivery.getDeliveryTime()).isEqualTo(changedDelivery.getDeliveryTime());
        assertThat(updatedDelivery.getPrice()).isEqualTo(changedDelivery.getPrice());
        assertThat(updatedDelivery.getDeliveryStatus()).isEqualTo(changedDelivery.getDeliveryStatus());

        assertThat(updatedDelivery.getOrder().getId()).isEqualTo(changedDelivery.getOrder().getId());
        assertThat(updatedDelivery.getCourier().getId()).isEqualTo(changedDelivery.getCourier().getId());

        // verify
        then(deliveryRepository)
                .should(times(1))
                .save(deliveryEntity);
    }

    private Delivery prepareDeliveryForEdit() {
        Long deliveryId = 1L;
        Double newPrice = 40.50D;
        LocalDateTime newDeliveryLocalDateTime = LocalDateTime.of(2023, 5, 28, 20, 30, 0);
        OffsetDateTime newDeliveryOffsetDateTime = newDeliveryLocalDateTime.atOffset(ZoneOffset.UTC);
        DeliveryStatus newDeliveryStatus = IN_TRANSIT;
        Long newOrderId = 3L;
        Long newCourierId = 3L;

        Delivery changedDelivery = new Delivery();
        changedDelivery.setId(deliveryId);
        changedDelivery.setPrice(newPrice);
        changedDelivery.setDeliveryTime(newDeliveryOffsetDateTime);
        changedDelivery.setDeliveryStatus(newDeliveryStatus);

        Order newOrder = new Order();
        newOrder.setId(newOrderId);
        Courier newCourier = new Courier();
        newCourier.setId(newCourierId);

        changedDelivery.setOrder(newOrder);
        changedDelivery.setCourier(newCourier);

        return changedDelivery;
    }

    private DeliveryEntity prepareDeliveryEntityForEdit() {
        Long deliveryId = 1L;
        BigDecimal newPrice = BigDecimal.valueOf(40.50D);
        LocalDateTime newDeliveryLocalDateTime = LocalDateTime.of(2023, 5, 28, 20, 30, 0);
        DeliveryStatus newDeliveryStatus = IN_TRANSIT;
        Long newOrderId = 3L;
        Long newCourierId = 3L;

        DeliveryEntity changedDeliveryEntity = new DeliveryEntity();
        changedDeliveryEntity.setId(deliveryId);
        changedDeliveryEntity.setPrice(newPrice);
        changedDeliveryEntity.setDeliveryTime(newDeliveryLocalDateTime);
        changedDeliveryEntity.setDeliveryStatus(newDeliveryStatus);

        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setId(newOrderId);
        CourierEntity newCourierEntity = new CourierEntity();
        newCourierEntity.setId(newCourierId);

        changedDeliveryEntity.setOrder(newOrderEntity);
        changedDeliveryEntity.setCourier(newCourierEntity);

        return changedDeliveryEntity;
    }
}