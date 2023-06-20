package com.kapusniak.tomasz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.Optional;


@NoRepositoryBean
public interface UuidRepository<T, UUID> extends JpaRepository<T, Long> {
    @NonNull
    Optional<T> findByUuid(@NonNull UUID uuid);

    @NonNull
    Page<T> findAllPagedByIdIn(@NonNull Iterable<Long> ids, @NonNull Pageable pageable);
}