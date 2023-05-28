package com.kapusniak.tomasz.entity;

import com.kapusniak.tomasz.openapi.model.CourierCompany;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@Table(name = "couriers")
public class CourierEntity extends BaseEntity {

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
    private List<DeliveryEntity> deliveryList = new ArrayList<>();

}