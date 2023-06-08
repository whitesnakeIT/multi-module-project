package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.mapper.TrackingEntityMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackingService {

    private final TrackingJpaRepository trackingRepository;

    private final TrackingEntityMapper trackingEntityMapper;

    @Transactional
    public Tracking save(Tracking tracking) {
        if (tracking == null) {
            throw new RuntimeException("Saving tracking failed. Tracking is null.");
        }
        TrackingEntity trackingEntity = trackingEntityMapper.mapToEntity(tracking);
        TrackingEntity savedEntity = trackingRepository.save(trackingEntity);

        return trackingEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Tracking> findAll() {
        return trackingRepository
                .findAll()
                .stream()
                .map(trackingEntityMapper::mapToApiModel)
                .toList();
    }

    public Tracking findByUuid(UUID trackingUuid) {
        if (trackingUuid == null) {
            throw new RuntimeException("Searching for tracking failed. Tracking uuid is null.");
        }
        return trackingEntityMapper.mapToApiModel(trackingRepository.findByUuid(trackingUuid)
                .orElseThrow(RuntimeException::new));
    }

    @Transactional
    public void delete(UUID trackingUuid) {
        if (trackingUuid == null) {
            throw new RuntimeException("Deleting tracking failed. Tracking uuid is null.");
        }
        Tracking tracking = findByUuid(trackingUuid);

        trackingRepository.delete(trackingEntityMapper.mapToEntity(tracking));
    }

    @Transactional
    public Tracking update(UUID uuid, Tracking tracking) {
        if (uuid == null) {
            throw new RuntimeException("Updating tracking failed. Tracking uuid is null.");
        }
        if (tracking == null) {
            throw new RuntimeException("Updating tracking failed. Tracking is null.");
        }

        Tracking trackingFromDb = findByUuid(uuid);

        Tracking updatedTracking = updateFields(trackingFromDb, tracking);

        TrackingEntity updatedTrackingEntity = trackingRepository
                .save(trackingEntityMapper.mapToEntity(updatedTracking));

        return trackingEntityMapper.mapToApiModel(updatedTrackingEntity);
    }

    private Tracking updateFields(Tracking trackingFromDb, Tracking newTracking) {
        if (newTracking.getUuid() == null) {
            newTracking.setUuid(trackingFromDb.getUuid());
        }
        if (!newTracking.getUuid().equals(trackingFromDb.getUuid())) {
            throw new RuntimeException("Updating tracking fields failed. Different uuid's");
        }
        return newTracking;
    }
}
