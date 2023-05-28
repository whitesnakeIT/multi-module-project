package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.mapstruct.TrackingEntityMapper;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackingService {

    private final TrackingJpaRepository trackingRepository;

    private final TrackingEntityMapper trackingEntityMapper;

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

    public Tracking findById(Long trackingId) {
        if (trackingId == null) {
            throw new RuntimeException("Searching for tracking failed. Tracking id is null.");
        }
        return trackingEntityMapper.mapToApiModel(trackingRepository.findById(trackingId)
                .orElseThrow(RuntimeException::new));
    }

    public void delete(Long trackingId) {
        if (trackingId == null) {
            throw new RuntimeException("Deleting tracking failed. Tracking id is null.");
        }
        Tracking tracking = findById(trackingId);

        trackingRepository.delete(trackingEntityMapper.mapToEntity(tracking));
    }

    public Tracking update(Long id, Tracking tracking) {
        if (id == null) {
            throw new RuntimeException("Updating tracking failed. Tracking id is null.");
        }
        if (tracking == null) {
            throw new RuntimeException("Updating tracking failed. Tracking is null.");
        }

        Tracking trackingFromDb = findById(id);

        Tracking updatedTracking = updateFields(trackingFromDb, tracking);

        TrackingEntity updatedTrackingEntity = trackingRepository
                .save(trackingEntityMapper.mapToEntity(updatedTracking));

        return trackingEntityMapper.mapToApiModel(updatedTrackingEntity);
    }

    private Tracking updateFields(Tracking trackingFromDb, Tracking newTracking) {
        if (newTracking.getId() == null) {
            newTracking.setId(trackingFromDb.getId());
        }
        if (!newTracking.getId().equals(trackingFromDb.getId())) {
            throw new RuntimeException("Updating tracking fields failed. Different id's");
        }
        return newTracking;
    }
}
