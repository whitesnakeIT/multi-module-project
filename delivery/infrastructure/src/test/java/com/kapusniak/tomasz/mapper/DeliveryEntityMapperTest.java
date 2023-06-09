package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.openapi.model.CourierCompany.DPD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DeliveryEntityMapperTest {

    @Autowired
    private DeliveryEntityMapper deliveryEntityMapper;

    public Delivery prepareDelivery() {
        LocalDateTime localDeliveryTime =
                LocalDateTime.of(2023, 5, 28, 12, 0, 0);
        OffsetDateTime offsetDeliveryTime =
                localDeliveryTime.atOffset(ZoneOffset.UTC);
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setPrice(20.50d);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveryTime(offsetDeliveryTime);
        delivery.setCourier(prepareCourier());
        delivery.setOrder(prepareOrder());
        delivery.setUuid(UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f"));
        return delivery;
    }

    public DeliveryEntity prepareDeliveryEntity() {
        LocalDateTime localDeliveryTime =
                LocalDateTime.of(2023, 5, 28, 12, 0, 0);
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setId(1L);
        deliveryEntity.setPrice(BigDecimal.valueOf(20.50d));
        deliveryEntity.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryEntity.setDeliveryTime(localDeliveryTime);
        deliveryEntity.setCourier(prepareCourierEntity());
        deliveryEntity.setOrder(prepareOrderEntity());
        deliveryEntity.setUuid(UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f"));

        return deliveryEntity;
    }

    public Courier prepareCourier() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setFirstName("testFirstName");
        courier.setLastName("testLastName");
        courier.setDeliveryList(List.of(new Delivery(), new Delivery()));
        courier.setCourierCompany(DPD);
        courier.setUuid(UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f"));

        return courier;
    }

    public CourierEntity prepareCourierEntity() {
        CourierEntity courierEntity = new CourierEntity();
        courierEntity.setId(1L);
        courierEntity.setFirstName("testFirstName");
        courierEntity.setLastName("testLastName");
        courierEntity.setDeliveryList(List.of(new DeliveryEntity(), new DeliveryEntity()));
        courierEntity.setCourierCompany(DPD);
        courierEntity.setUuid(UUID.fromString("fe362772-17c3-4547-b559-ceb13e164e6f"));

        return courierEntity;
    }

    public Order prepareOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setSenderAddress("testSenderAddress");
        order.setReceiverAddress("testReceiverAddress");
        order.setPackageType(PackageType.PARCEL);
        order.setPackageSize(PackageSize.SMALL);
        Customer customer = new Customer();
        customer.setOrders(List.of(new Order(), new Order()));
        order.setCustomer(customer);

        return order;
    }

    public OrderEntity prepareOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setSenderAddress("testSenderAddress");
        orderEntity.setReceiverAddress("testReceiverAddress");
        orderEntity.setPackageType(PackageType.PARCEL);
        orderEntity.setPackageSize(PackageSize.SMALL);
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setOrders(List.of(new OrderEntity(), new OrderEntity()));
        orderEntity.setCustomer(customerEntity);

        return orderEntity;
    }

    @Test
    @DisplayName("should map Delivery to DeliveryEntity with null Courier.DeliveryList" +
            " and ignored Order.Customer.Orders fields to avoid cycle dependencies")
    void mapToEntity() {
        // given
        Delivery delivery = prepareDelivery();

        // when
        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);

        // then
        assertThat(deliveryEntity.getUuid()).isEqualTo(delivery.getUuid());
        assertThat(deliveryEntity.getPrice()).isEqualTo(BigDecimal.valueOf(delivery.getPrice()));
        assertThat(deliveryEntity.getDeliveryStatus()).isEqualTo(delivery.getDeliveryStatus());

        assertThat(deliveryEntity.getCourier().getUuid()).isNotNull();
        assertThat(deliveryEntity.getCourier().getUuid()).isEqualTo(delivery.getCourier().getUuid());

        assertThat(deliveryEntity.getCourier().getDeliveryList()).isNull();
        assertThat(deliveryEntity.getOrder().getCustomer().getOrders()).isNull();
    }

    @Test
    @DisplayName("should map DeliveryEntity to Delivery with ignored Courier.DeliveryList" +
            " and Order.Customer.Orders fields to avoid cycle dependencies")
    void mapToApiModel() {
        // given
        DeliveryEntity deliveryEntity = prepareDeliveryEntity();

        // when
        Delivery delivery = deliveryEntityMapper.mapToApiModel(deliveryEntity);

        // then
        assertThat(delivery.getUuid()).isEqualTo(deliveryEntity.getUuid());
        assertThat(delivery.getPrice()).isEqualTo(deliveryEntity.getPrice().doubleValue());
        assertThat(delivery.getDeliveryStatus()).isEqualTo(deliveryEntity.getDeliveryStatus());

        assertThat(delivery.getCourier().getDeliveryList()).isNull();
        assertThat(delivery.getCourier().getUuid()).isEqualTo(deliveryEntity.getCourier().getUuid());
        assertThat(delivery.getOrder().getCustomer().getOrders()).isNull();
    }

}