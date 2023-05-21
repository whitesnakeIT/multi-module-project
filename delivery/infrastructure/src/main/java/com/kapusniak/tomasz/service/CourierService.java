package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.mapstruct.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
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
            throw new RuntimeException("Updati ng courier failed. Courier id is null.");
        }
        if (courier == null) {
            throw new RuntimeException("Updating courier failed. Courier is null.");
        }

        Courier courierFromDb = findById(id);

        // update

        CourierEntity updatedCourier = courierRepository
                .save(courierEntityMapper.mapToEntity(courierFromDb));

        return courierEntityMapper.mapToApiModel(updatedCourier);
    }

}
