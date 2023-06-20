package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.mapper.CourierEntityMapper;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.service.PagingService.PAGE_SIZE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourierService {

    private final CourierJpaRepository courierRepository;

    private final CourierEntityMapper courierEntityMapper;

    private final DeliveryService deliveryService;

    private final PagingService pagingService;

    @Transactional
    @CachePut(value = "couriers", key = "#courier.uuid")
    public Courier save(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Saving courier failed. Courier is null.");
        }
        CourierEntity courierEntity = courierEntityMapper.mapToEntity(courier);
        CourierEntity savedEntity = courierRepository.save(courierEntity);

        return courierEntityMapper.mapToApiModel(savedEntity);
    }

    @Cacheable(value = "couriers")
    public List<Courier> findAll(Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);
        List<CourierEntity> couriers = courierRepository
                .findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();
        List<Long> courierIds = couriers.stream()
                .map(CourierEntity::getId)
                .toList();
        List<DeliveryEntity> deliveries = deliveryService.findAllEntitiesByCourierIdIn(courierIds, pageNumber);
//
        couriers.forEach(courier -> courier
                .setDeliveryList(deliveryService.extractDeliveries(
                                deliveries, courier.getId()
                        )
                )
        );

        return couriers.stream()
                .map(courierEntityMapper::mapToApiModel)
                .toList();
    }

    @Cacheable(value = "couriers", key = "#courierUuid")
    public Courier findByUuid(UUID courierUuid) {
        if (courierUuid == null) {
            throw new EntityNotFoundException("Searching for courier failed. Courier uuid is null.");
        }
        return courierEntityMapper.mapToApiModel(courierRepository.findByUuid(courierUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Searching for courier failed. Unrecognized uuid " + courierUuid)));
    }

    @Transactional
    @CacheEvict(value = "couriers", key = "#courierUuid")
    public void delete(UUID courierUuid) {
        if (courierUuid == null) {
            throw new IllegalArgumentException("Deleting courier failed. Courier uuid is null.");
        }
        Courier courier = findByUuid(courierUuid);

        courierRepository.delete(courierEntityMapper.mapToEntity(courier));
    }

    @Transactional
    @CachePut(value = "couriers", key = "#uuid")
    public Courier update(UUID uuid, Courier courier) {
        if (uuid == null) {
            throw new IllegalArgumentException("Updating courier failed. Courier uuid is null.");
        }
        if (courier == null) {
            throw new IllegalArgumentException("Updating courier failed. Courier is null.");
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
            throw new IllegalArgumentException("Updating courier fields failed. Different uuid's");
        }
        return newCourier;
    }
}
