package com.kapusniak.tomasz.model;

import com.kapusniak.tomasz.enums.PackageSize;
import com.kapusniak.tomasz.enums.PackageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Order {

    private Long id;
    private String senderAddress;
    private String receiverAddress;
    private PackageType packageType;
    private PackageSize packageSize;
    private LocalDate preferredDeliveryDate;
    //    @JsonIgnore
    @ToString.Exclude
    private Customer customer;

}
