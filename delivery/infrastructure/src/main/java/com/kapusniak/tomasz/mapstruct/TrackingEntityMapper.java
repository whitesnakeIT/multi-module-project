package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.TrackingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackingEntityMapper {
//    TrackingEntity mapToEntity(Tracking tracking);

    TrackingEntity mapToEntity(com.kapusniak.tomasz.openapi.model.Tracking tracking);

//    Tracking mapToModel(TrackingEntity trackingEntity);

    com.kapusniak.tomasz.openapi.model.Tracking mapToApiModel(TrackingEntity trackingEntity);
}
