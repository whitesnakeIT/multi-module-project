package com.kapusniak.tomasz.adapter.service;

import com.kapusniak.tomasz.adapter.repository.DeliveryRepositoryAdapter;
import com.kapusniak.tomasz.model.Delivery;
import com.kapusniak.tomasz.ports.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceAdapter implements DeliveryService {

    private final DeliveryRepositoryAdapter deliveryRepository;

    @Override
    public Delivery save(Delivery delivery) {
        if (delivery == null) {
            throw new RuntimeException("Saving delivery failed. Delivery is null.");
        }

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery findById(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Searching for delivery failed. Delivery id is null.");
        }

        return deliveryRepository.findById(deliveryId);
    }

    @Override
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public void delete(Long deliveryId) {
        if (deliveryId == null) {
            throw new RuntimeException("Deleting delivery failed. Delivery id is null.");
        }
        Delivery delivery = findById(deliveryId);
        deliveryRepository.delete(delivery);
    }

    @Override
    public Delivery update(Delivery delivery) {
        return null;
    }
}
