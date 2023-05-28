package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.openapi.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = OrderEntityMapper.class
)
public interface CustomerEntityMapper {

    @Mapping(
            target = "orders",
            qualifiedByName = {"OrderEntityWithoutCustomer"}
    )
    CustomerEntity mapToEntity(Customer customer);

    @Mapping(
            target = "orders",
            qualifiedByName = {"OrderWithoutCustomer"}
    )
    Customer mapToApiModel(CustomerEntity customerEntity);

}
