
package com.kapusniak.tomasz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;


@NoRepositoryBean
public interface UuidRepository<T, UUID> extends JpaRepository<T, UUID> {
    Optional<T> findByUuid(UUID uuid);
}