package com.kapusniak.tomasz.adapter.repository;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.mapstruct.CourierEntityMapper;
import com.kapusniak.tomasz.model.Courier;
import com.kapusniak.tomasz.ports.repository.CourierRepository;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourierRepositoryAdapter implements CourierRepository {

    private final CourierJpaRepository courierJpaRepository;

    private final CourierEntityMapper courierEntityMapper;

    @Override
    public Courier save(Courier courier) {
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);
        CourierEntity savedEntity = courierJpaRepository.save(courierEntity);

        return courierEntityMapper
                .mapToModel(savedEntity);
    }

    @Override
    public Courier findById(Long id) {
        Optional<CourierEntity> courierEntityOptional = courierJpaRepository.findById(id);

        return courierEntityOptional
                .map(courierEntityMapper::mapToModel)
                .orElse(null);
    }

    @Override
    public List<Courier> findAll() {
        return courierJpaRepository
                .findAll()
                .stream()
                .map(courierEntityMapper::mapToModel)
                .toList();
    }

    @Override
    public void delete(Courier courier) {
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);
        courierJpaRepository.delete(courierEntity);
    }

    @Override
    public Courier update(Courier courier) {
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);
        CourierEntity updatedEntity = courierJpaRepository.save(courierEntity);
        return courierEntityMapper.mapToModel(updatedEntity);
    }

}
