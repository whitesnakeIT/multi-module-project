package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.Courier;
import com.kapusniak.tomasz.service.CourierService;
import lombok.RequiredArgsConstructor;
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
    public Courier createCourier(@RequestBody Courier courier) {
        return courierService.save(courier);
    }

    @PutMapping("/{id}")
    public Courier updateCourier(@PathVariable Long id, @RequestBody Courier courier) {
        return courierService.update(id, courier);
    }

    @DeleteMapping("/{id}")
    public void deleteCourier(@PathVariable Long id) {
        courierService.delete(id);
    }

}