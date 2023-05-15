package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Tracking;
import com.kapusniak.tomasz.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class TrackingService {


    private final TrackingRepository trackingRepository;

    public Tracking save(Tracking tracking) {
        if (tracking == null) {
            throw new RuntimeException("Saving tracking failed. Tracking is null.");
        }
        return trackingRepository.save(tracking);
    }

    public List<Tracking> findAll() {
        return trackingRepository.findAll();
    }

    public Tracking findById(Long trackingId) {
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
        Tracking tracking = findById(trackingId);
        trackingRepository.delete(tracking);
    }

    public Tracking update(Long id, Tracking tracking) {
        if (id == null) {
            throw new RuntimeException("Updating tracking failed. Tracking id is null.");
        }
        if (tracking == null) {
            throw new RuntimeException("Updating tracking failed. Tracking is null.");
        }

        Tracking trackingFromDb = findById(id);
        trackingFromDb.setLocalization(tracking.getLocalization());

        return trackingRepository.save(trackingFromDb);
    }
}
