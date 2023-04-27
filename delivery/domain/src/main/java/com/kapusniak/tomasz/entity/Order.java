package com.kapusniak.tomasz.entity;

import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "sender_address")
    private String senderAddress;

    @Column(name = "receiver_address")
    private String receiverAddress;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "package_type")
    private PackageType packageType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "package_size")
    private PackageSize packageSize;

    @Column(name = "preferred_delivery_date")
    private LocalDate preferredDeliveryDate;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
