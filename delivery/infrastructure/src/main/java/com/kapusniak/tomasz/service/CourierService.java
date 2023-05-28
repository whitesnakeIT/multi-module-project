package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.mapstruct.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourierService {

    private final CourierJpaRepository courierRepository;

    private final CourierEntityMapper courierEntityMapper;

    public Courier save(Courier courier) {
        if (courier == null) {
            throw new RuntimeException("Saving courier failed. Courier is null.");
        }
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);
        CourierEntity savedEntity = courierRepository.save(courierEntity);

        return courierEntityMapper.mapToApiModel(savedEntity);
    }

    public List<Courier> findAll() {
        return courierRepository
                .findAll()
                .stream()
                .map(courierEntityMapper::mapToApiModel)
                .toList();
    }

    public Courier findById(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Searching for courier failed. Courier id is null.");
        }
        return courierEntityMapper.mapToApiModel(courierRepository.findById(courierId)
                .orElseThrow(RuntimeException::new));
    }

    public void delete(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Deleting courier failed. Courier id is null.");
        }
        Courier courier = findById(courierId);

        courierRepository.delete(courierEntityMapper.mapToEntity(courier));
    }

    public Courier update(Long id, Courier courier) {
        if (id == null) {
            throw new RuntimeException("Updating courier failed. Courier id is null.");
        }
        if (courier == null) {
            throw new RuntimeException("Updating courier failed. Courier is null.");
        }

        Courier courierFromDb = findById(id);

        Courier updatedCourier = updateFields(courierFromDb, courier);

        CourierEntity updatedCourierEntity = courierRepository
                .save(courierEntityMapper.mapToEntity(updatedCourier));

        return courierEntityMapper.mapToApiModel(updatedCourierEntity);
    }

    private Courier updateFields(Courier courierFromDb, Courier newCourier) {
        if (newCourier.getId() == null) {
            newCourier.setId(courierFromDb.getId());
        }
        if (!newCourier.getId().equals(courierFromDb.getId())) {
            throw new RuntimeException("Updating courier fields failed. Different id's");
        }
        return newCourier;
    }
}
