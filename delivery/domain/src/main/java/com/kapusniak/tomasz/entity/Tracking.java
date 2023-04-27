package com.kapusniak.tomasz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity(name = "tracking")
@Getter
@Setter
@ToString
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tracking tracking = (Tracking) o;
        return getId() != null && Objects.equals(getId(), tracking.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}