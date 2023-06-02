package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.openapi.model.Delivery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.kapusniak.tomasz.openapi.model.CourierCompany.DPD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CourierEntityMapperTest {

    @Autowired
    private CourierEntityMapper courierEntityMapper;

    public Courier prepareCourier() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setFirstName("testFirstName");
        courier.setLastName("testLastName");
        courier.setDeliveryList(List.of(new Delivery(), new Delivery()));
        courier.setCourierCompany(DPD);

        return courier;
    }

    public CourierEntity prepareCourierEntity() {
        CourierEntity courierEntity = new CourierEntity();
        courierEntity.setId(1L);
        courierEntity.setFirstName("testFirstName");
        courierEntity.setLastName("testLastName");
        courierEntity.setDeliveryList(List.of(new DeliveryEntity(), new DeliveryEntity()));
        courierEntity.setCourierCompany(DPD);

        return courierEntity;
    }

    @Test
    @DisplayName("should map from Courier to CourierEntity with ignored" +
            " DeliveryList.Courier field to avoid cycle dependencies")
    void mapToEntity() {
        // given
        Courier courier = prepareCourier();

        // when
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);

        // then
        assertThat(courierEntity.getId()).isEqualTo(courier.getId());
        assertThat(courierEntity.getFirstName()).isEqualTo(courier.getFirstName());
        assertThat(courierEntity.getLastName()).isEqualTo(courier.getLastName());
        assertThat(courierEntity.getCourierCompany()).isEqualTo(courier.getCourierCompany());

        assertThat(courierEntity.getUuid()).isNotNull();
        assertThat(courierEntity.getDeliveryList()).isNotNull();
        assertThat(courierEntity.getDeliveryList().get(0).getCourier()).isNull();
        assertThat(courierEntity.getDeliveryList().get(1).getCourier()).isNull();
    }

    @Test
    @DisplayName("should map from CourierEntity to Courier with ignored" +
            " DeliveryList.Courier field to avoid cycle dependencies")

    void mapToApiModel() {
        // given
        CourierEntity courierEntity = prepareCourierEntity();

        // when
        Courier courier = courierEntityMapper.mapToApiModel(courierEntity);

        // then
        assertThat(courier.getId()).isEqualTo(courierEntity.getId());
        assertThat(courier.getFirstName()).isEqualTo(courierEntity.getFirstName());
        assertThat(courier.getLastName()).isEqualTo(courierEntity.getLastName());
        assertThat(courier.getCourierCompany()).isEqualTo(courierEntity.getCourierCompany());

        assertThat(courier.getDeliveryList()).isNotNull();
        assertThat(courier.getDeliveryList().get(0).getCourier()).isNull();
        assertThat(courier.getDeliveryList().get(1).getCourier()).isNull();

    }
}