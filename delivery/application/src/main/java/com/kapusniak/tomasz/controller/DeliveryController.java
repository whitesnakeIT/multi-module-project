package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.DeliveryEntity;
import com.kapusniak.tomasz.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public List<DeliveryEntity> getAllDeliveries() {
        return deliveryService.findAll();
    }

    @GetMapping("/{id}")
    public DeliveryEntity getDelivery(@PathVariable Long id) {
        return deliveryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<DeliveryEntity> createDelivery(@RequestBody DeliveryEntity delivery) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deliveryService.save(delivery));
    }

    @PutMapping("/{id}")
    public DeliveryEntity updateDelivery(@PathVariable Long id, @RequestBody DeliveryEntity delivery) {
        return deliveryService.update(id, delivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
