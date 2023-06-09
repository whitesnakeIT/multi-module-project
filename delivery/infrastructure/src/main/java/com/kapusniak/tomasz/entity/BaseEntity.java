package com.kapusniak.tomasz.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(of = "uuid")
@Getter
@Setter
public abstract class BaseEntity {

    private UUID uuid = UUID.randomUUID();

    @Version
    private int version;
}