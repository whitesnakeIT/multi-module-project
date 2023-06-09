package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.CustomerEntity;
import com.kapusniak.tomasz.openapi.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        uses = OrderEntityMapper.class
)
public interface CustomerEntityMapper {

    @Mapping(
            target = "uuid",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(
            target = "version",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    CustomerEntity mapToEntity(Customer customer);

    Customer mapToApiModel(CustomerEntity customerEntity);

}
