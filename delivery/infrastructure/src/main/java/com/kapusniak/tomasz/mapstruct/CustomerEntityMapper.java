package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {
    CustomerEntity mapToEntity(Customer courier);

    Customer mapToModel(CustomerEntity customerEntity);
}
