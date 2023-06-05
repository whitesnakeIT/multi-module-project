package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.openapi.model.Tracking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackingEntityMapper {
    TrackingEntity mapToEntity(Tracking tracking);

    Tracking mapToApiModel(TrackingEntity trackingEntity);
}
