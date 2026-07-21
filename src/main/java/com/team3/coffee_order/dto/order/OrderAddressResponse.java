package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.domain.entity.Order;
import lombok.Getter;

@Getter
public class OrderAddressResponse {

    private final Long orderId;
    private final String address;
    private final String zipCode;

    public OrderAddressResponse(Order order) {
        this.orderId = order.getId();
        this.address = order.getAddress();
        this.zipCode = order.getZipCode();
    }



}
