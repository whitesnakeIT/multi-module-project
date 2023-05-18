package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.CourierEntity;
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
    public List<CourierEntity> getAllCouriers() {
        return courierService.findAll();
    }

    @GetMapping("/{id}")
    public CourierEntity getCourier(@PathVariable Long id) {
        return courierService.findById(id);
    }

    @PostMapping

    public ResponseEntity<CourierEntity> createCourier(@RequestBody CourierEntity courier) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courierService.save(courier));

    }

    @PutMapping("/{id}")
    public CourierEntity updateCourier(@PathVariable Long id, @RequestBody CourierEntity courier) {
        return courierService.update(id, courier);
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();

    }

}