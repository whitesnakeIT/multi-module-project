package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.entity.Tracking;
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
    public List<Tracking> getAllTracking() {
        return trackingService.findAll();
    }

    @GetMapping("/{id}")
    public Tracking getTracking(@PathVariable Long id) {
        return trackingService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Tracking> createTracking(@RequestBody Tracking tracking) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trackingService.save(tracking));
    }

    @PutMapping("/{id}")
    public Tracking updateTracking(@PathVariable Long id, @RequestBody Tracking tracking) {
        return trackingService.update(id, tracking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracking(@PathVariable Long id) {
        trackingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
