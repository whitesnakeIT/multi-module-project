package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Mapper(
        componentModel = "spring",
        uses = {
                OrderEntityMapper.class,
        }
)
public interface DeliveryEntityMapper {
    @Mapping(
            target = "deliveryTime",
            qualifiedByName = "offsetToLocalDateTime"
    )
    @Mapping(
            target = "courier.deliveryList",
            expression = "java(null)"
    )
    @Mapping(
            target = "order.customer.orders",
            expression = "java(null)"
    )
    DeliveryEntity mapToEntity(Delivery delivery);

//    List<DeliveryEntity> mapToEntity(List<Delivery> delivery);

    @Mapping(
            target = "deliveryTime",
            qualifiedByName = "localDateTimeToOffset"
    )
    @Mapping(
            target = "courier.deliveryList",
            ignore = true
    )
    @Mapping(
            target = "order.customer.orders",
            ignore = true
    )
    Delivery mapToApiModel(DeliveryEntity deliveryEntity);

    @Named("DeliveryWithoutCourier")
    @Mapping(
            target = "deliveryTime",
            qualifiedByName = "localDateTimeToOffset"
    )
    @Mapping(
            target = "courier",
            ignore = true
    )
    @Mapping(
            target = "order.customer.orders",
            ignore = true
    )
    Delivery mapToApiModelWithoutCourier(DeliveryEntity deliveryEntity);

    @Named("DeliveryEntityWithoutCourier")
    @Mapping(
            target = "deliveryTime",
            qualifiedByName = "offsetToLocalDateTime"
    )
    @Mapping(
            target = "courier",
            expression = "java(null)"
    )
    @Mapping(
            target = "order.customer.orders",
            expression = "java(null)"
    )
    DeliveryEntity mapToEntityWithoutCourier(Delivery delivery);


    @Named("offsetToLocalDateTime")
    default LocalDateTime mapOffsetToLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
//            throw new RuntimeException("Mapping OffsetDateTime to LocalDateTime failed. OffsetDateTime is null.");
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

    @Named("localDateTimeToOffset")
    default OffsetDateTime mapLocalDateTimeToOffset(LocalDateTime localDateTime) {
        if (localDateTime == null) {
//            throw new RuntimeException("Mapping LocalDateTime to OffsetDateTime failed. LocalDateTime is null.");
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}
