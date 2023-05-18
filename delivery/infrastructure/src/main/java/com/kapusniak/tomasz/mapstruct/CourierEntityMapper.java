package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.model.Courier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierEntityMapper {
    CourierEntity mapToEntity(Courier courier);

    Courier mapToModel(CourierEntity courierEntity);
}
