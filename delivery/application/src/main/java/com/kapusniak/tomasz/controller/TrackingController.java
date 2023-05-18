package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.TrackingEntity;
import com.kapusniak.tomasz.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping
    public List<TrackingEntity> getAllTracking() {
        return trackingService.findAll();
    }

    @GetMapping("/{id}")
    public TrackingEntity getTracking(@PathVariable Long id) {
        return trackingService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TrackingEntity> createTracking(@RequestBody TrackingEntity tracking) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trackingService.save(tracking));
    }

    @PutMapping("/{id}")
    public TrackingEntity updateTracking(@PathVariable Long id, @RequestBody TrackingEntity tracking) {
        return trackingService.update(id, tracking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracking(@PathVariable Long id) {
        trackingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
