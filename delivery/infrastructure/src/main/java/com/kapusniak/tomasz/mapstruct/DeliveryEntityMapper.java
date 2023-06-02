package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(
        componentModel = "spring",
        uses = {
                OrderEntityMapper.class,
                TimeMapper.class
        }
)
public interface DeliveryEntityMapper {
    @Mapping(
            target = "courier.deliveryList",
            expression = "java(null)"
    )
    DeliveryEntity mapToEntity(Delivery delivery);

    @Mapping(
            target = "courier.deliveryList",
            ignore = true
    )
    Delivery mapToApiModel(DeliveryEntity deliveryEntity);

}
