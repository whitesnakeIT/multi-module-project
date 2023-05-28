package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.mapstruct.DeliveryEntityMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryJpaRepository deliveryRepository;

    private final DeliveryEntityMapper deliveryEntityMapper;

    public Delivery save(Delivery delivery) {
        if (delivery == null) {
            throw new RuntimeException("Saving delivery failed. Delivery is null.");
        }
        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);
        DeliveryEntity savedEntity = deliveryRepository.save(deliveryEntity);

        return deliveryEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Delivery> findAll() {
        return deliveryRepository
                .findAll()
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
    }

    public Delivery findById(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Searching for delivery failed. Delivery id is null.");
        }
        return deliveryEntityMapper.mapToApiModel(deliveryRepository.findById(deliveryId)
                .orElseThrow(RuntimeException::new));
    }

    public void delete(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Deleting delivery failed. Delivery id is null.");
        }
        Delivery delivery = findById(deliveryId);

        deliveryRepository.delete(deliveryEntityMapper.mapToEntity(delivery));
    }

    public Delivery update(Long id, Delivery delivery) {
        if (id == null) {
            throw new RuntimeException("Updating delivery failed. Delivery id is null.");
        }
        if (delivery == null) {
            throw new RuntimeException("Updating delivery failed. Delivery is null.");
        }

        Delivery deliveryFromDb = findById(id);

        Delivery updatedDelivery = updateFields(deliveryFromDb, delivery);

        DeliveryEntity updatedDeliveryEntity = deliveryRepository
                .save(deliveryEntityMapper.mapToEntity(updatedDelivery));

        return deliveryEntityMapper.mapToApiModel(updatedDeliveryEntity);
    }

    private Delivery updateFields(Delivery deliveryFromDb, Delivery newDelivery) {
        if (newDelivery.getId() == null) {
            newDelivery.setId(deliveryFromDb.getId());
        }
        if (!newDelivery.getId().equals(deliveryFromDb.getId())) {
            throw new RuntimeException("Updating delivery fields failed. Different id's");
        }
        return newDelivery;
    }

}
