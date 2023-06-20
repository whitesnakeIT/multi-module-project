package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.entity.OrderEntity;
import com.kapusniak.tomasz.mapper.DeliveryEntityMapper;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.repository.jpa.DeliveryJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.kapusniak.tomasz.service.PagingService.PAGE_SIZE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryJpaRepository deliveryRepository;

    private final DeliveryEntityMapper deliveryEntityMapper;

    private final OrderService orderService;

    private final PagingService pagingService;

    @Transactional
    @CachePut(value = "deliveries", key = "#delivery.uuid")
    public Delivery save(Delivery delivery) {
        if (delivery == null) {
            throw new IllegalArgumentException("Saving delivery failed. Delivery is null.");
        }
        DeliveryEntity deliveryEntity = deliveryEntityMapper.mapToEntity(delivery);
        DeliveryEntity savedEntity = deliveryRepository.save(deliveryEntity);

        return deliveryEntityMapper.mapToApiModel(savedEntity);
    }

    @Cacheable(value = "deliveries")
    public List<Delivery> findAll(Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);
//        List<Delivery> deliveries = deliveryRepository
        List<DeliveryEntity> deliveries = deliveryRepository
                .findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();
//                .stream()
//                .map(deliveryEntityMapper::mapToApiModel)
//                .toList();
        List<Long> orderIds = deliveries.stream()
                .map(delivery -> delivery.getOrder().getId())
                .toList();
//        List<Order> orders = orderService.findAllByIdIn(orderIds, pageNumber);
        List<OrderEntity> orderEntities = orderService.findAllEntitiesByIdIn(orderIds, pageNumber);
//deliveries.get(0).get//////
//        deliveries.forEach(delivery -> delivery.setOrder(
//                orderService.extractOrder(orders, delivery.getId())
//        ));

        return deliveries.stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
    }

    @Cacheable(value = "deliveries", key = "#deliveryUuid")
    public Delivery findByUuid(UUID deliveryUuid) {
        if (deliveryUuid == null) {
            throw new EntityNotFoundException("Searching for delivery failed. Delivery uuid is null.");
        }
        return deliveryEntityMapper.mapToApiModel(deliveryRepository.findByUuid(deliveryUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Searching for delivery failed. Unrecognized uuid " + deliveryUuid)));
    }

    @Transactional
    @CacheEvict(value = "delivery", key = "#deliveryUuid")
    public void delete(UUID deliveryUuid) {
        if (deliveryUuid == null) {
            throw new IllegalArgumentException("Deleting delivery failed. Delivery uuid is null.");
        }
        Delivery delivery = findByUuid(deliveryUuid);

        deliveryRepository.delete(deliveryEntityMapper.mapToEntity(delivery));
    }

    @Transactional
    @CachePut(value = "deliveries", key = "#uuid")
    public Delivery update(UUID uuid, Delivery delivery) {
        if (uuid == null) {
            throw new IllegalArgumentException("Updating delivery failed. Delivery uuid is null.");
        }
        if (delivery == null) {
            throw new IllegalArgumentException("Updating delivery failed. Delivery is null.");
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
            throw new IllegalArgumentException("Updating delivery fields failed. Different uuid's");
        }
        return newDelivery;
    }

    public List<Delivery> findAllByDeliveryStatus(DeliveryStatus deliveryStatus) {
        if (deliveryStatus == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery status is null.");
        }

        List<Delivery> deliveries = deliveryRepository
                .findAllByDeliveryStatus(deliveryStatus)
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
        return deliveries;
    }

    public List<Delivery> findAllByDeliveryStatus(DeliveryStatus deliveryStatus, Integer page) {
        if (deliveryStatus == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery status is null.");
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

        List<Delivery> deliveries = deliveryRepository
                .findAllByDeliveryStatus(deliveryStatus, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
        return deliveries;
    }

    public List<Delivery> findAllByDeliveryStatusAndDeliveryTimeBefore(DeliveryStatus deliveryStatus, LocalDateTime deliveryTime) {
        if (deliveryStatus == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery status is null.");
        }
        if (deliveryTime == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery time is null.");
        }
        return deliveryRepository
                .findAllByDeliveryStatusAndDeliveryTimeBefore(deliveryStatus, deliveryTime)
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
    }

    public List<Delivery> findAllByDeliveryStatusAndDeliveryTimeBefore(DeliveryStatus deliveryStatus, LocalDateTime deliveryTime, Integer page) {
        if (deliveryStatus == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery status is null.");
        }
        if (deliveryTime == null) {
            throw new EntityNotFoundException("Searching for deliveries failed. Delivery time is null.");
        }
        Integer pageNumber = pagingService.validatePageNumber(page);
        return deliveryRepository
                .findAllByDeliveryStatusAndDeliveryTimeBefore(deliveryStatus, deliveryTime, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();
    }

    //    public List<Delivery> findAllByCourierIdIn(List<Long> courierIds, Integer page) {
//        Integer pageNumber = pagingService.validatePageNumber(page);
//        return deliveryRepository.findAllByCourierIdIn(courierIds, PageRequest.of(pageNumber,PAGE_SIZE))
//                .stream()
//                .map(deliveryEntityMapper::mapToApiModel)
//                .toList();
//
//    }
    public List<DeliveryEntity> findAllEntitiesByCourierIdIn(List<Long> courierIds, Integer page) {
        if (courierIds == null) {
            throw new RuntimeException();
        }
        Integer pageNumber = pagingService.validatePageNumber(page);

        List<DeliveryEntity> deliveryEntitiesByCourierId = deliveryRepository
                .findAllByCourierIdIn(courierIds, PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();


        List<Long> orderIds = deliveryEntitiesByCourierId
                .stream()
                .map(delivery -> delivery.getOrder().getId())
                .toList();

        List<OrderEntity> orderEntities = orderService.findAllEntitiesByIdIn(orderIds, pageNumber);

        deliveryEntitiesByCourierId.forEach(deliveryEntity ->
                deliveryEntity
                        .setOrder(orderService
                                .extractOrderEntities(orderEntities, deliveryEntity.getId())));


        return deliveryEntitiesByCourierId;

    }

    public List<Delivery> findAllByOrderIdIn(List<Long> orderIds, Integer page) {
        Integer pageNumber = pagingService.validatePageNumber(page);
        return deliveryRepository.findAllByOrderIdIn(orderIds, PageRequest.of(pageNumber, PAGE_SIZE))
                .stream()
                .map(deliveryEntityMapper::mapToApiModel)
                .toList();

    }

    //    public List<Delivery> extractDeliveries(List<Delivery> deliveries, Long courierId) {
//        if (deliveries == null) {
//            throw new RuntimeException();
//        }
//        if (courierId == null) {
//            throw new RuntimeException();
//        }
//        return deliveries.stream()
//                .filter(delivery -> delivery.getCourier().getId().equals(courierId))
//                .toList();
//    }
    public List<DeliveryEntity> extractDeliveries(List<DeliveryEntity> deliveryEntities, Long courierId) {
        if (deliveryEntities == null) {
            throw new RuntimeException();
        }
        if (courierId == null) {
            throw new RuntimeException();
        }
        return deliveryEntities.stream()
                .filter(delivery -> delivery.getCourier().getId().equals(courierId))
                .toList();
    }

}
