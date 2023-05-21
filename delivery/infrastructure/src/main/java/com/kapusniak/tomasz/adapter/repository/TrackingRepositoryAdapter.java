//package com.kapusniak.tomasz.adapter.repository;
//
//import com.kapusniak.tomasz.entity.TrackingEntity;
//import com.kapusniak.tomasz.mapstruct.TrackingEntityMapper;
//import com.kapusniak.tomasz.model.Tracking;
//import com.kapusniak.tomasz.ports.repository.TrackingRepository;
//import com.kapusniak.tomasz.repository.jpa.TrackingJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class TrackingRepositoryAdapter implements TrackingRepository {
//
//    private final TrackingJpaRepository trackingJpaRepository;
//
//    private final TrackingEntityMapper trackingEntityMapper;
//
//    @Override
//    public Tracking save(Tracking tracking) {
//        TrackingEntity trackingEntity = trackingEntityMapper.mapToEntity(tracking);
//        TrackingEntity savedEntity = trackingJpaRepository.save(trackingEntity);
//
//        return trackingEntityMapper
//                .mapToModel(savedEntity);
//    }
//
//    @Override
//    public Tracking findById(Long id) {
//        Optional<TrackingEntity> trackingEntityOptional = trackingJpaRepository.findById(id);
//
//        return trackingEntityOptional
//                .map(trackingEntityMapper::mapToModel)
//                .orElse(null);
//    }
//
//    @Override
//    public List<Tracking> findAll() {
//        return trackingJpaRepository
//                .findAll()
//                .stream()
//                .map(trackingEntityMapper::mapToModel)
//                .toList();
//    }
//
//    @Override
//    public void delete(Tracking tracking) {
//        TrackingEntity trackingEntity = trackingEntityMapper.mapToEntity(tracking);
//        trackingJpaRepository.delete(trackingEntity);
//    }
//
//    @Override
//    public Tracking update(Tracking tracking) {
//        TrackingEntity trackingEntity = trackingEntityMapper.mapToEntity(tracking);
//        TrackingEntity updatedEntity = trackingJpaRepository.save(trackingEntity);
//        return trackingEntityMapper.mapToModel(updatedEntity);
//    }
//
//}
