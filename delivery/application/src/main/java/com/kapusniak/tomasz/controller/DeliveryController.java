package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.Delivery;
import com.kapusniak.tomasz.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryService.findAll();
    }

    @GetMapping("/{id}")
    public Delivery getDelivery(@PathVariable Long id) {
        return deliveryService.findById(id);
    }

    @PostMapping
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        return deliveryService.save(delivery);
    }

    @PutMapping("/{id}")
    public Delivery updateDelivery(@PathVariable Long id, @RequestBody Delivery delivery) {
        return deliveryService.update(id, delivery);
    }

    @DeleteMapping("/{id}")
    public void deleteDelivery(@PathVariable Long id) {
        deliveryService.delete(id);
    }

}
