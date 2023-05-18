package com.kapusniak.tomasz.adapter.service;

import com.kapusniak.tomasz.adapter.repository.CourierRepositoryAdapter;
import com.kapusniak.tomasz.model.Courier;
import com.kapusniak.tomasz.ports.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierServiceAdapter implements CourierService {

    private final CourierRepositoryAdapter courierRepository;

    @Override
    public Courier save(Courier courier) {
        if (courier == null) {
            throw new RuntimeException("Saving courier failed. Courier is null.");
        }

        return courierRepository.save(courier);
    }

    @Override
    public Courier findById(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Searching for courier failed. Courier id is null.");
        }

        return courierRepository.findById(courierId);
    }

    @Override
    public List<Courier> findAll() {
        return courierRepository.findAll();
    }

    @Override
    public void delete(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Deleting courier failed. Courier id is null.");
        }
        Courier courier = findById(courierId);
        courierRepository.delete(courier);
    }

    @Override
    public Courier update(Courier courier) {
        return null;
    }
}
