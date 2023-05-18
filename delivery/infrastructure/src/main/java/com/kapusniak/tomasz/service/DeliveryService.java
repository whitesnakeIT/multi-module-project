package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class DeliveryService {

    private final DeliveryJpaRepository deliveryRepository;

    public DeliveryEntity save(DeliveryEntity delivery) {
        if (delivery == null) {
            throw new RuntimeException("Saving delivery failed. Delivery is null.");
        }
        return deliveryRepository.save(delivery);
    }

    public List<DeliveryEntity> findAll() {
        return deliveryRepository.findAll();
    }

    public DeliveryEntity findById(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Searching for delivery failed. Delivery id is null.");
        }
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Deleting delivery failed. Delivery id is null.");
        }
        DeliveryEntity delivery = findById(deliveryId);
        deliveryRepository.delete(delivery);
    }

    public DeliveryEntity update(Long id, DeliveryEntity delivery) {
        if (id == null) {
            throw new RuntimeException("Updating delivery failed. Delivery id is null.");
        }
        if (delivery == null) {
            throw new RuntimeException("Updating delivery failed. Delivery is null.");
        }

        DeliveryEntity deliveryFromDb = findById(id);
        deliveryFromDb.setPrice(delivery.getPrice());
        deliveryFromDb.setDeliveryStatus(delivery.getDeliveryStatus());
        deliveryFromDb.setDeliveryTime(delivery.getDeliveryTime());

        return deliveryRepository.save(deliveryFromDb);
    }
}
