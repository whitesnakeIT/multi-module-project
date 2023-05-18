package com.kapusniak.tomasz.ports.repository;

import com.kapusniak.tomasz.model.Tracking;

import java.util.List;

public interface TrackingRepository {
    Tracking save(Tracking tracking);

    Tracking findById(Long id);

    List<Tracking> findAll();

    void delete(Tracking tracking);

    Tracking update(Tracking tracking);
}
