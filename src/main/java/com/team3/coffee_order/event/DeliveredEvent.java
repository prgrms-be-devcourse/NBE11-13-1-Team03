package com.team3.coffee_order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliveredEvent {

    private final Long orderId;
    private final String customerEmail;
    private final String address;
    private final String zipCode;
    private final LocalDateTime deliveredAt;
    private final Duration processingDuration;
}
