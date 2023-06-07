package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.mapper.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.CourierCompany;
import com.kapusniak.tomasz.openapi.model.Delivery;
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
import java.util.UUID;

import static com.kapusniak.tomasz.openapi.model.CourierCompany.DHL;
import static com.kapusniak.tomasz.openapi.model.CourierCompany.FEDEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
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
        courier.setCourierCompany(DHL);
        courier.setFirstName("testFirstName");
        courier.setLastName("testLastName");

        courierList.add(courier);
        courierList.add(courier);

        courierEntity.setId(1L);
        courierEntity.setCourierCompany(DHL);
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
    void findByUuid() {

        // given
        given(courierRepository.findByUuid(
                any(UUID.class)))
                .willReturn(Optional.of(courierEntity));
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");

        // when
        Courier courierByUuid = courierService.findByUuid(courierUuid);

        // then
        assertThat(courierByUuid.getUuid())
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when courier id is null")
    void findByUuidNull() {

        // given
        UUID courierUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.findByUuid(courierUuid));

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
        given(courierRepository.findByUuid(
                any(UUID.class)))
                .willReturn(Optional.of(courierEntity));
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");

        // when
        courierService.delete(courierUuid);

        // then
        then(courierRepository)
                .should(times(1))
                .delete(courierEntity);
    }

    @Test
    @DisplayName("should throw an exception when courier id is null")
    void deleteNull() {
        // given
        UUID courierUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.delete(courierUuid));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting courier failed. Courier id is null.");
    }

    @Test
    @DisplayName("should throw an exception when id is null")
    void updateNullUuid() {
        // given
        UUID courierUuid = null;

        // when
        Throwable throwable = catchThrowable(() ->
                courierService.update(courierUuid, courier));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating courier failed. Courier id is null.");
    }

    @Test
    @DisplayName("should throw an exception when courier is null")
    void updateNullCourier() {
        // given
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");
        Courier courier = null;

        // when
        Throwable thrown = catchThrowable(() ->
                courierService.update(courierUuid, courier));

        // then
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating courier failed. Courier is null.");
    }

    @Test
    @DisplayName("should throw an exception when newCourier's id doesn't match courierFromDb's id")
    void updateIdMissMatch() {
        // given
        Courier newCourier = new Courier();

        UUID oldUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");
        UUID newUuid = UUID.fromString("7ded2601-a101-4958-8924-130a17278a20");
        newCourier.setUuid(newUuid);

        // and
        when(courierRepository.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(courierEntity));
        // when
        Throwable throwable = catchThrowable(() ->
                courierService.update(oldUuid, newCourier));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating courier fields failed. Different id's");
    }

    @Test
    @DisplayName("should correctly update courier when valid id and courier are provided")
    void shouldUpdateCourier() {
        // given
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");
        Courier changedCourier = prepareCourierToEdit();
        CourierEntity changedCourierEntity = prepareCourierEntityToEdit();

        // and
        when(courierRepository.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(courierEntity));
        when(courierRepository.save(any(CourierEntity.class)))
                .thenReturn(changedCourierEntity);

        //and
        when(courierEntityMapper
                .mapToApiModel(changedCourierEntity))
                .thenReturn(changedCourier);

        // when
        Courier updatedCourier = courierService.update(courierUuid, changedCourier);

        // then
        assertThat(updatedCourier).isNotNull();
        assertThat(updatedCourier.getUuid()).isEqualTo(courierUuid);
        assertThat(updatedCourier.getFirstName()).isEqualTo(changedCourier.getFirstName());
        assertThat(updatedCourier.getLastName()).isEqualTo(changedCourier.getLastName());
        assertThat(updatedCourier.getCourierCompany()).isEqualTo(changedCourier.getCourierCompany());
        assertThat(updatedCourier.getDeliveryList().get(0).getUuid()).isEqualTo(changedCourier.getDeliveryList().get(0).getUuid());
        assertThat(updatedCourier.getDeliveryList().get(1).getUuid()).isEqualTo(changedCourier.getDeliveryList().get(1).getUuid());

        // verify
        then(courierRepository)
                .should(times(1))
                .save(courierEntity);
    }

    private CourierEntity prepareCourierEntityToEdit() {
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        CourierCompany newCourierCompany = FEDEX;

        UUID newDelivery1Uuid = UUID.fromString("31822712-94b3-43ed-9aac-24613948ca79");
        UUID newDelivery2Uuid = UUID.fromString("1f263424-a92a-49a6-b38f-eaa2861ab332");

        Long newDelivery1Id = 3L;
        Long newDelivery2Id = 4L;

        CourierEntity changedCourierEntity = new CourierEntity();
        changedCourierEntity.setUuid(courierUuid);
        changedCourierEntity.setFirstName(newFirstName);
        changedCourierEntity.setLastName(newLastName);
        changedCourierEntity.setCourierCompany(newCourierCompany);

        DeliveryEntity newDeliveryEntity1 = new DeliveryEntity();
        newDeliveryEntity1.setId(newDelivery1Id);
        newDeliveryEntity1.setUuid(newDelivery1Uuid);
        DeliveryEntity newDeliveryEntity2 = new DeliveryEntity();
        newDeliveryEntity2.setId(newDelivery2Id);
        newDeliveryEntity2.setUuid(newDelivery2Uuid);

        changedCourierEntity.setDeliveryList(List.of(newDeliveryEntity1, newDeliveryEntity2));

        return changedCourierEntity;
    }

    private Courier prepareCourierToEdit() {
        UUID courierUuid = UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f");
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        CourierCompany newCourierCompany = FEDEX;

        UUID newDelivery1Uuid = UUID.fromString("31822712-94b3-43ed-9aac-24613948ca79");
        UUID newDelivery2Uuid = UUID.fromString("1f263424-a92a-49a6-b38f-eaa2861ab332");

        Courier changedCourier = new Courier();
        changedCourier.setUuid(courierUuid);
        changedCourier.setFirstName(newFirstName);
        changedCourier.setLastName(newLastName);
        changedCourier.setCourierCompany(newCourierCompany);

        Delivery newDelivery1 = new Delivery();
        newDelivery1.setUuid(newDelivery1Uuid);
        Delivery newDelivery2 = new Delivery();
        newDelivery2.setUuid(newDelivery2Uuid);

        changedCourier.setDeliveryList(List.of(newDelivery1, newDelivery2));

        return changedCourier;
    }
}