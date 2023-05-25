package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;


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
    DeliveryEntity mapToEntity(Delivery delivery);

    List<DeliveryEntity> mapToEntity(List<Delivery> delivery);

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
    @Mapping
            (target = "courier",
                    ignore = true)
    Delivery mapToApiModelWithoutCourier(DeliveryEntity deliveryEntity);


    @Named("offsetToLocalDateTime")
    default LocalDateTime mapOffsetToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toLocalDateTime();
    }

    @Named("localDateTimeToOffset")
    default OffsetDateTime mapLocalDateTimeToOffset(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}
