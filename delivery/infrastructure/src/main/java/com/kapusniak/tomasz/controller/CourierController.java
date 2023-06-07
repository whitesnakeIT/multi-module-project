package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.CouriersApi;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CourierController implements CouriersApi {

    private final CourierService courierService;

    @Override
    public ResponseEntity<Courier> createCourier(@RequestBody @Valid Courier courier) {
        Courier save = courierService.save(courier);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteCourier(@PathVariable("uuid") UUID courierUuid) {
        courierService.delete(courierUuid);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<List<Courier>> getAllCouriers() {
        List<Courier> courierList = courierService.findAll();

        return ResponseEntity.ok(courierList);
    }

    @Override
    public ResponseEntity<Courier> getCourier(@PathVariable("uuid") UUID courierUuid) {
        Courier courier = courierService.findByUuid(courierUuid);

        return ResponseEntity.ok(courier);
    }

    @Override
    public ResponseEntity<Courier> updateCourier(@PathVariable("uuid") UUID courierUuid, @RequestBody @Valid Courier courier) {
        Courier update = courierService.update(courierUuid, courier);

        return ResponseEntity.ok(update);
    }
}
