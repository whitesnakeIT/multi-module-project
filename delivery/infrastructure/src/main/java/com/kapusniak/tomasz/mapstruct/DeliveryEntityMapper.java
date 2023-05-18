package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.model.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryEntityMapper {
    DeliveryEntity mapToEntity(Delivery customer);

    Delivery mapToModel(DeliveryEntity customerEntity);
}
