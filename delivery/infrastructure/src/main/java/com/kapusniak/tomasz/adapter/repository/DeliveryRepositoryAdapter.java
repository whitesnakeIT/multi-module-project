//package com.kapusniak.tomasz.adapter.repository;
//
//import com.kapusniak.tomasz.entity.DeliveryEntity;
//import com.kapusniak.tomasz.mapstruct.DeliveryEntityMapper;
//import com.kapusniak.tomasz.model.Delivery;
//import com.kapusniak.tomasz.ports.repository.DeliveryRepository;
//import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class DeliveryRepositoryAdapter implements DeliveryRepository {
//
//    private final DeliveryJpaRepository deliveryJpaRepository;
//
//    private final DeliveryEntityMapper deliveryEntityMapper;
//
//    @Override
//    public Delivery save(Delivery delivery) {
//        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);
//        DeliveryEntity savedEntity = deliveryJpaRepository.save(deliveryEntity);
//
//        return deliveryEntityMapper
//                .mapToModel(savedEntity);
//    }
//
//    @Override
//    public Delivery findById(Long id) {
//        Optional<DeliveryEntity> deliveryEntityOptional = deliveryJpaRepository.findById(id);
//
//        return deliveryEntityOptional
//                .map(deliveryEntityMapper::mapToModel)
//                .orElse(null);
//    }
//
//    @Override
//    public List<Delivery> findAll() {
//        return deliveryJpaRepository
//                .findAll()
//                .stream()
//                .map(deliveryEntityMapper::mapToModel)
//                .toList();
//    }
//
//    @Override
//    public void delete(Delivery delivery) {
//        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);
//        deliveryJpaRepository.delete(deliveryEntity);
//    }
//
//    @Override
//    public Delivery update(Delivery delivery) {
//        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);
//        DeliveryEntity updatedEntity = deliveryJpaRepository.save(deliveryEntity);
//        return deliveryEntityMapper.mapToModel(updatedEntity);
//    }
//
//}
