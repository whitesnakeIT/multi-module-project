package com.kapusniak.tomasz.ports.service;

import com.kapusniak.tomasz.model.Courier;

import java.util.List;

public interface CourierService {
    Courier save(Courier courier);

    Courier findById(Long id);

    List<Courier> findAll();

    void delete(Long courierId);

    Courier update(Courier courier);
}