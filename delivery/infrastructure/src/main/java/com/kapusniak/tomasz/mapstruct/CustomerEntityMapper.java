package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapstruct.annotation.DoIgnore;
import com.kapusniak.tomasz.mapstruct.context.CycleAvoidingMappingContext;
import com.kapusniak.tomasz.openapi.model.Customer;
import com.kapusniak.tomasz.openapi.model.Order;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"//,
        // uses = OrderEntityMapper.class
)
public interface CustomerEntityMapper {


    CustomerEntity mapToEntity(Customer customer, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Customer mapToApiModel(CustomerEntity customerEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    OrderEntity mapOrderToEntity(Order order, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Order mapEntityToOrder(OrderEntity orderEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default CustomerEntity mapToEntity(Customer customer) {
        return mapToEntity(customer, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Customer mapToApiModel(CustomerEntity customerEntity) {
        return mapToApiModel(customerEntity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default OrderEntity mapToEntity(Order order) {
        return mapOrderToEntity(order, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Order mapToApiModel(OrderEntity orderEntity) {
        return mapEntityToOrder(orderEntity, new CycleAvoidingMappingContext());
    }


}
