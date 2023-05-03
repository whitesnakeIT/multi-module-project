package com.kapusniak.tomasz.entity;

import com.kapusniak.tomasz.enums.CourierCompany;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@Table(name = "couriers", schema = "test")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courier_id", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "courier_company")
    private CourierCompany courierCompany;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ToString.Exclude
    @OneToMany(mappedBy = "courier",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Delivery> deliveryList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Courier courier = (Courier) o;
        return getId() != null && Objects.equals(getId(), courier.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}