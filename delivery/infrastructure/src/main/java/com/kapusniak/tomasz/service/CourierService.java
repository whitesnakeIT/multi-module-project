package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.mapper.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourierService {

    private final CourierJpaRepository courierRepository;

    private final CourierEntityMapper courierEntityMapper;

    @Transactional
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

    public Courier findByUuid(UUID courierUuid) {
        if (courierUuid == null) {
            throw new RuntimeException("Searching for courier failed. Courier uuid is null.");
        }
        return courierEntityMapper.mapToApiModel(courierRepository.findByUuid(courierUuid)
                .orElseThrow(RuntimeException::new));
    }

    @Transactional
    public void delete(UUID courierUuid) {
        if (courierUuid == null) {
            throw new RuntimeException("Deleting courier failed. Courier uuid is null.");
        }
        Courier courier = findByUuid(courierUuid);

        courierRepository.delete(courierEntityMapper.mapToEntity(courier));
    }

    @Transactional
    public Courier update(UUID uuid, Courier courier) {
        if (uuid == null) {
            throw new RuntimeException("Updating courier failed. Courier uuid is null.");
        }
        if (courier == null) {
            throw new RuntimeException("Updating courier failed. Courier is null.");
        }

        Courier courierFromDb = findByUuid(uuid);

        Courier updatedCourier = updateFields(courierFromDb, courier);

        CourierEntity updatedCourierEntity = courierRepository
                .save(courierEntityMapper.mapToEntity(updatedCourier));

        return courierEntityMapper.mapToApiModel(updatedCourierEntity);
    }

    private Courier updateFields(Courier courierFromDb, Courier newCourier) {
        if (newCourier.getUuid() == null) {
            newCourier.setUuid(courierFromDb.getUuid());
        }
        if (!newCourier.getUuid().equals(courierFromDb.getUuid())) {
            throw new RuntimeException("Updating courier fields failed. Different uuid's");
        }
        return newCourier;
    }
}
