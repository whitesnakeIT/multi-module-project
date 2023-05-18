package com.kapusniak.tomasz.model;

import com.kapusniak.tomasz.enums.CourierCompany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
public class Courier {

    private Long id;
    private CourierCompany courierCompany;
    private String firstName;
    private String lastName;
    private List<Delivery> deliveryList = new ArrayList<>();

}