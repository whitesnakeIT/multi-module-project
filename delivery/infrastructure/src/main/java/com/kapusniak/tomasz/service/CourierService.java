package com.kapusniak.tomasz.service;

import com.kapusniak.tomasz.entity.CourierEntity;
import com.kapusniak.tomasz.repository.jpa.CourierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional
public class CourierService {

    private final CourierJpaRepository courierRepository;

    public CourierEntity save(CourierEntity courier) {
        if (courier == null) {
            throw new RuntimeException("Saving courier failed. Courier is null.");
        }
        return courierRepository.save(courier);
    }

    public List<CourierEntity> findAll() {
        return courierRepository.findAll();
    }

    public CourierEntity findById(Long courierId) {
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
        CourierEntity courier = findById(courierId);
        courierRepository.delete(courier);
    }

    public CourierEntity update(Long id, CourierEntity courier) {
        if (id == null) {
            throw new RuntimeException("Updating courier failed. Courier id is null.");
        }
        if (courier == null) {
            throw new RuntimeException("Updating courier failed. Courier is null.");
        }

        CourierEntity courierFromDb = findById(id);
        courierFromDb.setFirstName(courier.getFirstName());
        courierFromDb.setLastName(courier.getLastName());

        return courierRepository.save(courierFromDb);
    }

}
