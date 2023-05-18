package com.kapusniak.tomasz.ports.service;

import com.kapusniak.tomasz.model.Tracking;

import java.util.List;

public interface TrackingService {
    Tracking save(Tracking tracking);

    Tracking findById(Long id);

    List<Tracking> findAll();

    void delete(Long trackingId);

    Tracking update(Tracking tracking);
}
