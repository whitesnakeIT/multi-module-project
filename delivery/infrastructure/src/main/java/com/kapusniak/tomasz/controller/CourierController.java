package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.CouriersApi;
import com.kapusniak.tomasz.openapi.model.Courier;
import com.kapusniak.tomasz.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CourierController implements CouriersApi {

    private final CourierService courierService;


    @Override
    public ResponseEntity<Courier> createCourier(@RequestBody Courier courier) {
        Courier save = courierService.save(courier);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteCourier(Long courierId) {
        courierService.delete(courierId);

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
    public ResponseEntity<Courier> getCourier(Long courierId) {
        Courier courier = courierService.findById(courierId);
        return ResponseEntity.ok(courier);
    }

    @Override
    public ResponseEntity<Courier> updateCourier(Long id, Courier courier) {
        return null;
    }
}
