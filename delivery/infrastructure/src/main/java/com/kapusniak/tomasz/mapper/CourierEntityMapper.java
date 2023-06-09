package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.openapi.model.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        uses = {
                DeliveryEntityMapper.class,
        }
)
public interface CourierEntityMapper {
    @Mapping(
            target = "deliveryList.courier",
            ignore = true
    )
    @Mapping(
            target = "uuid",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(
            target = "version",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    CourierEntity mapToEntity(Courier courier);

    @Mapping(
            target = "deliveryList.courier",
            ignore = true
    )
    Courier mapToApiModel(CourierEntity courierEntity);
  
}
