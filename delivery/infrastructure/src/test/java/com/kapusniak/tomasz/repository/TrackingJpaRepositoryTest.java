package com.kapusniak.tomasz.repository;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application-test.properties")
@SqlGroup(
        @Sql(
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:h2-scripts/cleanup.sql",
                        "classpath:h2-scripts/insert-data.sql"
                }
        )
)
public class TrackingJpaRepositoryTest {

    @Autowired
    private TrackingJpaRepository trackingRepository;

    TrackingEntity prepareTrackingEntity() {
        TrackingEntity trackingEntity = new TrackingEntity();
        trackingEntity.setLocalization("test localization");

        return trackingEntity;
    }

    @Test
    @DisplayName("should correctly set version number after saving " +
            "or editing an Order entity")
    @Transactional(propagation = NOT_SUPPORTED)
    void versionChecking() {
        // given
        TrackingEntity trackingEntity = prepareTrackingEntity();

        // when
        TrackingEntity savedTracking = trackingRepository.save(trackingEntity);

        // and
        savedTracking.setLocalization("new localization");

        TrackingEntity editedTracking = trackingRepository.save(savedTracking);

        // then
        assertThat(trackingEntity.getVersion()).isEqualTo(0);

        assertThat(savedTracking.getId()).isNotNull();
        assertThat(savedTracking.getVersion()).isEqualTo(0);

        assertThat(editedTracking.getId()).isEqualTo(savedTracking.getId());
        assertThat(editedTracking.getVersion()).isEqualTo(savedTracking.getVersion() + 1);
    }
}