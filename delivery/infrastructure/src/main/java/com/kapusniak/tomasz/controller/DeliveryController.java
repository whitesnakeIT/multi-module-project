package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.DeliveriesApi;
import com.kapusniak.tomasz.openapi.model.Delivery;
import com.kapusniak.tomasz.service.DeliveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DeliveryController implements DeliveriesApi {

    private final DeliveryService deliveryService;

    @Override
    public ResponseEntity<Delivery> createDelivery(@RequestBody @Valid Delivery delivery) {
        Delivery save = deliveryService.save(delivery);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteDelivery(@PathVariable("id") @Min(1) Long deliveryId) {
        deliveryService.delete(deliveryId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveryList = deliveryService.findAll();

        return ResponseEntity.ok(deliveryList);
    }

    @Override
    public ResponseEntity<Delivery> getDelivery(@PathVariable("id") @Min(1) Long deliveryId) {
        Delivery delivery = deliveryService.findById(deliveryId);

        return ResponseEntity.ok(delivery);
    }

    @Override
    public ResponseEntity<Delivery> updateDelivery(@PathVariable("id") @Min(1) Long deliveryId, @RequestBody @Valid Delivery delivery) {
        Delivery update = deliveryService.update(deliveryId, delivery);

        return ResponseEntity.ok(update);
    }
}
