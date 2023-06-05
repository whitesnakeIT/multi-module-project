package com.kapusniak.tomasz.mapper;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.openapi.model.Tracking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TrackingEntityMapperTest {

    @Autowired
    private TrackingEntityMapper trackingEntityMapper;

    @Test
    @DisplayName("should map Tracking to TrackingEntity")
    public void mapToEntity() {
        // given
        Tracking tracking = new Tracking();
        tracking.setId(1L);
        tracking.setLocalization("testLocalization");

        // when
        TrackingEntity trackingEntity = trackingEntityMapper.mapToEntity(tracking);

        // then
        assertThat(trackingEntity).isNotNull();
        assertThat(tracking.getId()).isEqualTo(trackingEntity.getId());
        assertThat(tracking.getLocalization()).isEqualTo(trackingEntity.getLocalization());
        assertThat(trackingEntity.getUuid()).isNotNull();
    }

    @Test
    @DisplayName("should map TrackingEntity to Tracking")
    public void mapToApiModel() {
        // given
        TrackingEntity entity = new TrackingEntity();
        entity.setId(1L);
        entity.setLocalization("123456789");

        // when
        Tracking tracking = trackingEntityMapper.mapToApiModel(entity);

        // then
        assertThat(tracking).isNotNull();
        assertThat(entity.getId()).isEqualTo(tracking.getId());
        assertThat(entity.getLocalization()).isEqualTo(tracking.getLocalization());
    }
}