package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface DeliveryEntityMapper {
//    DeliveryEntity mapToEntity(Delivery customer);

    @Mapping(
            target = "deliveryTime",
            expression = "java(mapOffsetToLocalDateTime(delivery.getDeliveryTime()))"
    )
    DeliveryEntity mapToEntity(com.kapusniak.tomasz.openapi.model.Delivery delivery);

//    Delivery mapToModel(DeliveryEntity customerEntity);

    @Mapping(
            target = "deliveryTime",
            expression = "java(mapLocalDateTimeToOffset(deliveryEntity.getDeliveryTime()))"
    )
    com.kapusniak.tomasz.openapi.model.Delivery mapToApiModel(DeliveryEntity deliveryEntity);

    default LocalDateTime mapOffsetToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toLocalDateTime();
    }

    default OffsetDateTime mapLocalDateTimeToOffset(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}
