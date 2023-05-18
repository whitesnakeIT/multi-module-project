package com.kapusniak.tomasz.ports.repository;

import com.kapusniak.tomasz.model.Courier;

import java.util.List;

public interface CourierRepository {
    Courier save(Courier courier);

    Courier findById(Long id);

    List<Courier> findAll();

    void delete(Courier courier);

    Courier update(Courier courier);
}