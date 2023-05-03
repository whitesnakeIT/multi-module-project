package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Delivery;
import com.kapusniak.tomasz.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public void save(Delivery delivery) {
        if (delivery == null) {
            throw new RuntimeException("Saving delivery failed. Delivery is null.");
        }
        deliveryRepository.save(delivery);
    }

    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    public Delivery findById(Long deliveryId) {
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
        Delivery delivery = findById(deliveryId);
        deliveryRepository.delete(delivery);
    }

}
