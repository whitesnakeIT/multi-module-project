package com.kapusniak.tomasz.mapstruct;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.model.Tracking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackingEntityMapper {
    TrackingEntity mapToEntity(Tracking tracking);

    Tracking mapToModel(TrackingEntity trackingEntity);
}
