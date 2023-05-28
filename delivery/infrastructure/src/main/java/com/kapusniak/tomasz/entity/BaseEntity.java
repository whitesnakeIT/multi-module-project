package com.kapusniak.tomasz.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(of = "uuid")
@Getter
@Setter
public abstract class BaseEntity {

    private String uuid = UUID.randomUUID().toString();
}