package com.kapusniak.tomasz.ports.repository;

import com.kapusniak.tomasz.model.Delivery;

import java.util.List;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Delivery findById(Long id);

    List<Delivery> findAll();

    void delete(Delivery delivery);

    Delivery update(Delivery delivery);
}