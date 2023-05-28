package com.kapusniak.tomasz.entity;

import com.kapusniak.tomasz.openapi.model.PackageSize;
import com.kapusniak.tomasz.openapi.model.PackageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

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
    private CustomerEntity customer;

}
