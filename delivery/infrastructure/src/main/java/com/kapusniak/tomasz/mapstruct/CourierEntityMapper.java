package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.openapi.model.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                DeliveryEntityMapper.class,
                OrderEntityMapper.class
        }
)
public interface CourierEntityMapper {
    @Mapping(
            target = "deliveryList",
            qualifiedByName = "DeliveryEntityWithoutCourier"
    )
    CourierEntity mapToEntity(Courier courier);

    @Mapping(
            target = "deliveryList",
            qualifiedByName = "DeliveryWithoutCourier"
    )
    Courier mapToApiModel(CourierEntity courierEntity);


}
