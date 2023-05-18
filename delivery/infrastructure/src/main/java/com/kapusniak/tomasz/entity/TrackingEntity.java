package com.kapusniak.tomasz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tracking")
public class TrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id", nullable = false)
    private Long id;

    private String localization;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrackingEntity tracking = (TrackingEntity) o;
        return getId() != null && Objects.equals(getId(), tracking.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}