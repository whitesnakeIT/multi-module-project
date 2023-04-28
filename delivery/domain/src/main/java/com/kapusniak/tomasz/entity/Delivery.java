package com.kapusniak.tomasz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "deliveries", schema = "test")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Delivery delivery = (Delivery) o;
        return getId() != null && Objects.equals(getId(), delivery.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}