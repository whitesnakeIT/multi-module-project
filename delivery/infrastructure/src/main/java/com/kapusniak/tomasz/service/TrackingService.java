package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class TrackingService {


    private final TrackingJpaRepository trackingRepository;

    public TrackingEntity save(TrackingEntity tracking) {
        if (tracking == null) {
            throw new RuntimeException("Saving tracking failed. Tracking is null.");
        }
        return trackingRepository.save(tracking);
    }

    public List<TrackingEntity> findAll() {
        return trackingRepository.findAll();
    }

    public TrackingEntity findById(Long trackingId) {
        if (trackingId == null) {
            throw new RuntimeException("Searching for tracking failed. Tracking id is null.");
        }
        return trackingRepository.findById(trackingId)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long trackingId) {
        if (trackingId == null) {
            throw new RuntimeException("Deleting tracking failed. Tracking id is null.");
        }
        TrackingEntity tracking = findById(trackingId);
        trackingRepository.delete(tracking);
    }

    public TrackingEntity update(Long id, TrackingEntity tracking) {
        if (id == null) {
            throw new RuntimeException("Updating tracking failed. Tracking id is null.");
        }
        if (tracking == null) {
            throw new RuntimeException("Updating tracking failed. Tracking is null.");
        }

        TrackingEntity trackingFromDb = findById(id);
        trackingFromDb.setLocalization(tracking.getLocalization());

        return trackingRepository.save(trackingFromDb);
    }
}
