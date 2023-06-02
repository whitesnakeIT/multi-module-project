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

    CustomerEntity mapToEntity(Customer customer);

    Customer mapToApiModel(CustomerEntity customerEntity);

}
