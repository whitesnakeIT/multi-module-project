package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.openapi.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {

    @Named("OrderEntityWithCustomerWithoutOrderList")
//    @Mapping(
//            target = "customer.orders",
//            expression = "java(null)"
//            ignore = true
//    )
    OrderEntity mapToEntity(Order order);

    @Named("OrderWithCustomerWithoutOrderList")
    @Mapping(
            target = "customer.orders",
            ignore = true
    )
    Order mapToApiModel(OrderEntity orderEntity);

    @Named("OrderWithoutCustomer")
    @Mapping(
            target = "customer",
            ignore = true
    )
    Order mapToApiModelWithoutCustomer(OrderEntity orderEntity);

    @Named("OrderEntityWithoutCustomer")
    @Mapping(
            target = "customer",
            ignore = true
    )
    OrderEntity mapToEntityWithoutCustomer(Order order);

}
