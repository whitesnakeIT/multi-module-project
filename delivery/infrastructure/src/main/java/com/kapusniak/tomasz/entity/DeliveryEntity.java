package com.kapusniak.tomasz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kapusniak.tomasz.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "deliveries")
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private CourierEntity courier;

    @ToString.Exclude
    @OneToOne
    @PrimaryKeyJoinColumn
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliveryEntity delivery = (DeliveryEntity) o;
        return getId() != null && Objects.equals(getId(), delivery.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}