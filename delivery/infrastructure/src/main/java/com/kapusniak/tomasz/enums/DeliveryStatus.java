package com.kapusniak.tomasz.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeliveryStatus {

    CREATED,

    PICKED_UP,

    IN_TRANSIT,

    DELIVERED,

    FAILED_DELIVERY_ATTEMPT,

    RETURNED,

    LOST
}
