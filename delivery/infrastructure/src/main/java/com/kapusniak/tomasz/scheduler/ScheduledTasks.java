package com.kapusniak.tomasz.scheduler;

import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.openapi.model.DeliveryStatus;
import com.kapusniak.tomasz.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


@Profile({"!test", "!jdbc", "!integration-test"})
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final DeliveryService deliveryService;

    @Scheduled(initialDelayString = "${app.scheduler.initial-delay-ms}", fixedRate = 500)
    @Transactional
    @Async
    public void createDeliveryTestData() {
        Delivery delivery = new Delivery();
        delivery.setDeliveryTime(OffsetDateTime.now().minusDays(3));
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);

        deliveryService.save(delivery);

        log.info("Delivery saved.");
    }

    @Scheduled(initialDelayString = "${app.scheduler.initial-delay-ms}", fixedRate = 2000)
    @Transactional
    @Async
    public void updateStatusToLostAndSaveToDb() {
        try {
            List<Delivery> deliveredDeliveries = deliveryService
                    .findAllByDeliveryStatus(DeliveryStatus.DELIVERED);
            deliveredDeliveries
                    .stream()
                    .peek(delivery -> delivery.setDeliveryStatus(DeliveryStatus.LOST))
                    .forEach(deliveryService::save);

            log.info("{} deliveries updated to lost", deliveredDeliveries.size());
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.error("Locking error occurred during updateStatusToLostAndSaveToDb. Sending details:");
        }
    }

    @Scheduled(initialDelayString = "${app.scheduler.initial-delay-ms}", fixedRate = 2000)
    @Transactional
    @Async
    public void updateStatusToDeliveredAndSaveToDb() {
        try {
            List<Delivery> deliveredDeliveries = deliveryService
                    .findAllByDeliveryStatus(DeliveryStatus.LOST);
            deliveredDeliveries
                    .stream()
                    .peek(delivery -> delivery.setDeliveryStatus(DeliveryStatus.DELIVERED))
                    .forEach(deliveryService::save);

            log.info("{} deliveries updated to delivered", deliveredDeliveries.size());
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.error("Locking error occurred during updateStatusToDeliveredAndSaveToDb. Sending details:");
        }
    }


    @Scheduled(fixedRate = 3000)
    @Transactional
    public void deleteDeliveredOlderThan2Days() {
        try {
            List<Delivery> deliveryList = deliveryService
                    .findAllByDeliveryStatusAndDeliveryTimeBefore(
                            DeliveryStatus.DELIVERED,
                            LocalDateTime.
                                    now()
                                    .minusDays(2));

            Thread.sleep(2000);
            if (deliveryList.isEmpty()) {
                log.info("No delivered deliveries older than 2 days were found to be deleted.");
            } else {
                log.warn("Found {} delivered deliveries older than 2 days to be deleted.", deliveryList.size());

                deliveryList.forEach(delivery -> {
                    deliveryService.delete(delivery.getUuid());
                });
            }
            log.info("{} deliveries deleted.", deliveryList.size());
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.warn("Locking error occurred during deleteDeliveredOlderThan2Days. Sending details:");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}