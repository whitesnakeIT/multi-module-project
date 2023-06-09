package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {

    @Mapping(
            target = "customer.orders",
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
    OrderEntity mapToEntity(Order order);

    @Mapping(
            target = "customer.orders",
            ignore = true
    )
    Order mapToApiModel(OrderEntity orderEntity);

}
