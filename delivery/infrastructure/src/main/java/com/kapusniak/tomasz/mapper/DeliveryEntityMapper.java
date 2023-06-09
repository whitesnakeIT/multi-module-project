package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.openapi.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;


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
    @Mapping(
            target = "uuid",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(
            target = "version",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    DeliveryEntity mapToEntity(Delivery delivery);


    @Mapping(
            target = "courier.deliveryList",
            ignore = true
    )
    Delivery mapToApiModel(DeliveryEntity deliveryEntity);

}
