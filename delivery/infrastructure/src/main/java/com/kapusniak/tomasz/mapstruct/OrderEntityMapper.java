package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
//    OrderEntity mapToEntity(Order order);

    OrderEntity mapToEntity(com.kapusniak.tomasz.openapi.model.Order order);

//    Order mapToModel(OrderEntity orderEntity);

    com.kapusniak.tomasz.openapi.model.Order mapToApiModel(OrderEntity orderEntity);
}
