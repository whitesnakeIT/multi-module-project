package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CourierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DeliveryEntityMapper.class)
public interface CourierEntityMapper {
//    CourierEntity mapToEntity(Courier courier);

    CourierEntity mapToEntity(com.kapusniak.tomasz.openapi.model.Courier courier);

//    Courier mapToModel(CourierEntity courierEntity);

    com.kapusniak.tomasz.openapi.model.Courier mapToApiModel(CourierEntity courierEntity);
}
