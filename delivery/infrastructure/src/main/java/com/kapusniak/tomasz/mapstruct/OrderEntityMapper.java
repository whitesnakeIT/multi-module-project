package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
    OrderEntity mapToEntity(Order order);

    Order mapToModel(OrderEntity orderEntity);
}
