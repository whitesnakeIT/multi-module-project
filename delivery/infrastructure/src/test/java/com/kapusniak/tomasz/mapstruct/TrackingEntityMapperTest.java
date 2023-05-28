package com.kapusniak.tomasz.mapstruct;

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
    @DisplayName("should map Tracking to TrackingEntity and add random uuid")
    public void mapToEntity() {
        // given
        Tracking tracking = new Tracking();
        tracking.setId(1L);
        tracking.setLocalization("testLocalization");

        // when
        TrackingEntity entity = trackingEntityMapper.mapToEntity(tracking);

        // then
        assertThat(entity).isNotNull();
        assertThat(tracking.getId()).isEqualTo(entity.getId());
        assertThat(tracking.getLocalization()).isEqualTo(entity.getLocalization());
        assertThat(entity.getUuid()).isNotNull();
    }

    @Test
    @DisplayName("should map TrackingEntity to Tracking without uuid")
    public void mapToApiModel() {
        // given
        TrackingEntity entity = new TrackingEntity();
        entity.setId(1L);
        entity.setLocalization("123456789");
        entity.setUuid("3208be6f-eacc-4da3-8761-e80a60404a5b");

        // when
        Tracking tracking = trackingEntityMapper.mapToApiModel(entity);

        // then
        assertThat(tracking).isNotNull();
        assertThat(entity.getId()).isEqualTo(tracking.getId());
        assertThat(entity.getLocalization()).isEqualTo(tracking.getLocalization());
    }
}