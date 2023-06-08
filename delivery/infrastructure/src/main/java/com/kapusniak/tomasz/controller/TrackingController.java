package com.kapusniak.tomasz.controller;

import com.kapusniak.tomasz.openapi.api.TrackingApi;
import com.kapusniak.tomasz.openapi.model.Tracking;
import com.kapusniak.tomasz.service.TrackingService;
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
public class TrackingController implements TrackingApi {

    private final TrackingService trackingService;

    @Override
    public ResponseEntity<Tracking> createTracking(@RequestBody @Valid Tracking tracking) {
        Tracking save = trackingService.save(tracking);

        return ResponseEntity.status(201).body(save);
    }

    @Override
    public ResponseEntity<Void> deleteTracking(@PathVariable("uuid") UUID trackingUuid) {
        trackingService.delete(trackingUuid);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<List<Tracking>> getAllTracking() {
        List<Tracking> trackingList = trackingService.findAll();

        return ResponseEntity.ok(trackingList);
    }

    @Override
    public ResponseEntity<Tracking> getTracking(@PathVariable("uuid") UUID trackingUuid) {
        Tracking tracking = trackingService.findByUuid(trackingUuid);

        return ResponseEntity.ok(tracking);
    }

    @Override
    public ResponseEntity<Tracking> updateTracking(@PathVariable("uuid") UUID trackingUuid, @RequestBody @Valid Tracking tracking) {
        Tracking update = trackingService.update(trackingUuid, tracking);

        return ResponseEntity.ok(update);
    }
}
