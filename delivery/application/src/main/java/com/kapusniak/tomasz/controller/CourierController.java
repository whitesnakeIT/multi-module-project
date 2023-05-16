package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.Courier;
import com.kapusniak.tomasz.service.CourierService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @GetMapping
    public List<Courier> getAllCouriers() {
        return courierService.findAll();
    }

    @GetMapping("/{id}")
    public Courier getCourier(@PathVariable Long id) {
        return courierService.findById(id);
    }

    @PostMapping

    public ResponseEntity<Courier> createCourier(@RequestBody Courier courier) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courierService.save(courier));

    }

    @PutMapping("/{id}")
    public Courier updateCourier(@PathVariable Long id, @RequestBody Courier courier) {
        return courierService.update(id, courier);
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();

    }

}