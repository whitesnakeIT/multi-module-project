package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {
//    CustomerEntity mapToEntity(Customer customer);

    CustomerEntity mapToEntity(com.kapusniak.tomasz.openapi.model.Customer customer);

//    Customer mapToModel(CustomerEntity customerEntity);

    com.kapusniak.tomasz.openapi.model.Customer mapToApiModel(CustomerEntity customerEntity);
}
