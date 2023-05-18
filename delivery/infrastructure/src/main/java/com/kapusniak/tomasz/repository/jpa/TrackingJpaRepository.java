package com.kapusniak.tomasz.repository.jpa;


import com.kapusniak.tomasz.entity.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface TrackingJpaRepository extends JpaRepository<TrackingEntity, Long> {
}
