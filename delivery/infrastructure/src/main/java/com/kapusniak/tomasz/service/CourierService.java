package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.Courier;
import com.kapusniak.tomasz.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class CourierService {

    private final CourierRepository courierRepository;

    public Courier save(Courier courier) {
        if (courier == null) {
            throw new RuntimeException("Saving courier failed. Courier is null.");
        }
        return courierRepository.save(courier);
    }

    public List<Courier> findAll() {
        return courierRepository.findAll();
    }

    public Courier findById(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Searching for courier failed. Courier id is null.");
        }
        return courierRepository.findById(courierId)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long courierId) {
        if (courierId == null) {
            throw new RuntimeException("Deleting courier failed. Courier id is null.");
        }
        Courier courier = findById(courierId);
        courierRepository.delete(courier);
    }

    public Courier update(Long id, Courier courier) {
        if (id == null) {
            throw new RuntimeException("Updating courier failed. Courier id is null.");
        }
        if (courier == null) {
            throw new RuntimeException("Updating courier failed. Courier is null.");
        }

        Courier courierFromDb = findById(id);
        courierFromDb.setFirstName(courier.getFirstName());
        courierFromDb.setLastName(courier.getLastName());

        return courierRepository.save(courierFromDb);
    }

}
