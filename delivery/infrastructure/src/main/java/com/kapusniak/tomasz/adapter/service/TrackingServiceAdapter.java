//package com.kapusniak.tomasz.adapter.service;
//
//import com.kapusniak.tomasz.adapter.repository.TrackingRepositoryAdapter;
//import com.kapusniak.tomasz.model.Tracking;
//import com.kapusniak.tomasz.ports.service.TrackingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class TrackingServiceAdapter implements TrackingService {
//
//    private final TrackingRepositoryAdapter trackingRepository;
//
//    @Override
//    public Tracking save(Tracking tracking) {
//        if (tracking == null) {
//            throw new RuntimeException("Saving tracking failed. Tracking is null.");
//        }
//
//        return trackingRepository.save(tracking);
//    }
//
//    @Override
//    public Tracking findById(Long trackingId) {
//        if (trackingId == null) {
//            throw new RuntimeException("Searching for tracking failed. Tracking id is null.");
//        }
//
//        return trackingRepository.findById(trackingId);
//    }
//
//    @Override
//    public List<Tracking> findAll() {
//        return trackingRepository.findAll();
//    }
//
//    @Override
//    public void delete(Long trackingId) {
//        if (trackingId == null) {
//            throw new RuntimeException("Deleting tracking failed. Tracking id is null.");
//        }
//        Tracking tracking = findById(trackingId);
//        trackingRepository.delete(tracking);
//    }
//
//    @Override
//    public Tracking update(Tracking tracking) {
//        return null;
//    }
//}
