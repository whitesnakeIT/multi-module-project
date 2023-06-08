package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.mapper.DeliveryEntityMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryJpaRepository deliveryRepository;

    private final DeliveryEntityMapper deliveryEntityMapper;

    @Transactional
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

    public Delivery findByUuid(UUID deliveryUuid) {
        if (deliveryUuid == null) {
            throw new RuntimeException("Searching for delivery failed. Delivery uuid is null.");
        }
        return deliveryEntityMapper.mapToApiModel(deliveryRepository.findByUuid(deliveryUuid)
                .orElseThrow(RuntimeException::new));
    }

    @Transactional
    public void delete(UUID deliveryUuid) {
        if (deliveryUuid == null) {
            throw new RuntimeException("Deleting delivery failed. Delivery uuid is null.");
        }
        Delivery delivery = findByUuid(deliveryUuid);

        deliveryRepository.delete(deliveryEntityMapper.mapToEntity(delivery));
    }

    @Transactional
    public Delivery update(UUID uuid, Delivery delivery) {
        if (uuid == null) {
            throw new RuntimeException("Updating delivery failed. Delivery uuid is null.");
        }
        if (delivery == null) {
            throw new RuntimeException("Updating delivery failed. Delivery is null.");
        }

        Delivery deliveryFromDb = findByUuid(uuid);

        Delivery updatedDelivery = updateFields(deliveryFromDb, delivery);

        DeliveryEntity updatedDeliveryEntity = deliveryRepository
                .save(deliveryEntityMapper.mapToEntity(updatedDelivery));

        return deliveryEntityMapper.mapToApiModel(updatedDeliveryEntity);
    }

    private Delivery updateFields(Delivery deliveryFromDb, Delivery newDelivery) {
        if (newDelivery.getUuid() == null) {
            newDelivery.setUuid(deliveryFromDb.getUuid());
        }
        if (!newDelivery.getUuid().equals(deliveryFromDb.getUuid())) {
            throw new RuntimeException("Updating delivery fields failed. Different uuid's");
        }
        return newDelivery;
    }

}
