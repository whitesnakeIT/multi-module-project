package com.kapusniak.tomasz.model;

import com.kapusniak.tomasz.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Delivery {

    private Long id;
    private DeliveryStatus deliveryStatus;
    private BigDecimal price;
    private LocalDateTime deliveryTime;
    //    @JsonIgnore
    @ToString.Exclude
    private Courier courier;
    @ToString.Exclude
    private Order order;

}