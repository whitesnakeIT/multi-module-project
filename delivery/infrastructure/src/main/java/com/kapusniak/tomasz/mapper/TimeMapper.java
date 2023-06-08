package com.kapusniak.tomasz.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(
        componentModel = "spring"
)
public interface TimeMapper {

    default LocalDateTime mapOffsetToLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
//            throw new RuntimeException("Mapping OffsetDateTime to LocalDateTime failed. OffsetDateTime is null.");
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

    default OffsetDateTime mapLocalDateTimeToOffset(LocalDateTime localDateTime) {
        if (localDateTime == null) {
//            throw new RuntimeException("Mapping LocalDateTime to OffsetDateTime failed. LocalDateTime is null.");
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}
