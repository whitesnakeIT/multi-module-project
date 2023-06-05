package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.mapper.TrackingEntityMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
class TrackingServiceTest {

    Tracking tracking = new Tracking();
    TrackingEntity trackingEntity = new TrackingEntity();
    List<Tracking> trackingList = new ArrayList<>();
    List<TrackingEntity> trackingEntityList = new ArrayList<>();
    @Mock
    private TrackingJpaRepository trackingRepository;
    @Mock
    private TrackingEntityMapper trackingEntityMapper;

    @InjectMocks
    private TrackingService trackingService;

    @BeforeEach
    void setup() {
        tracking.setId(1L);
        tracking.localization("testLocalization");

        trackingList.add(tracking);
        trackingList.add(tracking);

        trackingEntity.setId(1L);
        trackingEntity.setLocalization("testLocalization");

        trackingEntityList.add(trackingEntity);
        trackingEntityList.add(trackingEntity);

        when(trackingEntityMapper
                .mapToEntity(any(Tracking.class)))
                .thenReturn(trackingEntity);
        when(trackingEntityMapper
                .mapToApiModel(any(TrackingEntity.class)))
                .thenReturn(tracking);
    }

    @Test
    @DisplayName("should correctly save an Tracking entity exactly once")
    void save() {

        //given
        when(trackingRepository
                .save(any(TrackingEntity.class)))
                .thenReturn(trackingEntity);

        // when
        Tracking result = trackingService.save(tracking);

        // then
        then(trackingRepository)
                .should(times(1))
                .save(trackingEntityMapper.mapToEntity(tracking));

        // verify
        assertThat(tracking).isEqualTo(result);
    }

    @Test
    @DisplayName("should throw an exception when Tracking entity is null and check method invocations")
    void saveNull() {

        // given
        tracking = null;

        // when
        Throwable throwable = catchThrowable(() ->
                trackingService.save(tracking));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Saving tracking failed. Tracking is null.");

        // verify
        then(trackingRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should return list of all tracking with correct size and check method invocations")
    void findAll() {

        // given
        given(trackingRepository.findAll())
                .willReturn(trackingEntityList);

        // when
        List<Tracking> allTracking = trackingService.findAll();

        // then
        assertThat(allTracking.size())
                .isEqualTo(2);

        // verify
        then(trackingRepository)
                .should(times(1))
                .findAll();
    }

    @Test
    @DisplayName("should return tracking based on tracking id")
    void findById() {

        // given
        given(trackingRepository.findById(
                anyLong()))
                .willReturn(Optional.of(trackingEntity));
        Long trackingId = 1L;

        // when
        Tracking trackingById = trackingService.findById(trackingId);

        // then
        assertThat(trackingById.getId())
                .isNotNull();
    }

    @Test
    @DisplayName("should throw an exception when tracking id is null")
    void findByIdNull() {

        // given
        Long trackingId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                trackingService.findById(trackingId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Searching for tracking failed. Tracking id is null.");

        // verify
        then(trackingRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("should delete an tracking based on tracking id")
    void delete() {

        // given
        given(trackingRepository.findById(
                anyLong()))
                .willReturn(Optional.of(trackingEntity));
        Long trackingId = 1L;

        // when
        trackingService.delete(trackingId);

        // then
        then(trackingRepository)
                .should(times(1))
                .delete(trackingEntity);
    }

    @Test
    @DisplayName("should throw an exception when tracking id is null")
    void deleteNull() {
        // given
        Long trackingId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                trackingService.delete(trackingId));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Deleting tracking failed. Tracking id is null.");
    }

    @Test
    @DisplayName("should throw an exception when id is null")
    void updateNullId() {
        // given
        Long trackingId = null;

        // when
        Throwable throwable = catchThrowable(() ->
                trackingService.update(trackingId, tracking));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating tracking failed. Tracking id is null.");
    }

    @Test
    @DisplayName("should throw an exception when tracking is null")
    void updateNullTracking() {
        // given
        Long trackingId = 1L;
        Tracking tracking = null;

        // when
        Throwable thrown = catchThrowable(() ->
                trackingService.update(trackingId, tracking));

        // then
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating tracking failed. Tracking is null.");
    }

    @Test
    @DisplayName("should throw an exception when newTracking's id doesn't match trackingFromDb's id")
    void updateIdMissMatch() {
        // given
        Tracking newTracking = new Tracking();
        Long oldId = 1L;
        Long newId = 2L;
        newTracking.setId(newId);

        // and
        when(trackingRepository.findById(anyLong()))
                .thenReturn(Optional.of(trackingEntity));
        // when
        Throwable throwable = catchThrowable(() ->
                trackingService.update(oldId, newTracking));

        // then
        assertThat(throwable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Updating tracking fields failed. Different id's");
    }

    @Test
    @DisplayName("should correctly update tracking when valid id and tracking are provided")
    void shouldUpdateTracking() {
        // given
        Long trackingId = 1L;
        Tracking changedTracking = prepareTrackingForEdit();
        TrackingEntity changedTrackingEntity = prepareTrackingEntityForEdit();

        // and
        when(trackingRepository.findById(anyLong()))
                .thenReturn(Optional.of(trackingEntity));
        when(trackingRepository.save(any(TrackingEntity.class)))
                .thenReturn(changedTrackingEntity);

        //and
        when(trackingEntityMapper
                .mapToApiModel(changedTrackingEntity))
                .thenReturn(changedTracking);

        // when
        Tracking updatedTracking = trackingService.update(trackingId, changedTracking);

        // then
        assertThat(updatedTracking).isNotNull();
        assertThat(updatedTracking.getId()).isEqualTo(changedTracking.getId());
        assertThat(updatedTracking.getLocalization()).isEqualTo(changedTracking.getLocalization());

        // verify
        then(trackingRepository)
                .should(times(1))
                .save(trackingEntity);
    }

    private Tracking prepareTrackingForEdit() {
        Long trackingId = 1L;
        String newLocalization = "newLocalization";

        Tracking changedTracking = new Tracking();
        changedTracking.setId(trackingId);
        changedTracking.setLocalization(newLocalization);

        return changedTracking;
    }

    private TrackingEntity prepareTrackingEntityForEdit() {
        Long trackingId = 1L;
        String newLocalization = "newLocalization";

        TrackingEntity changedTrackingEntity = new TrackingEntity();
        changedTrackingEntity.setId(trackingId);
        changedTrackingEntity.setLocalization(newLocalization);

        return changedTrackingEntity;
    }
}