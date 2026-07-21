package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.domain.entity.Order;
import lombok.Getter;

@Getter
public class OrderAddressResponseDto {

    private final Long orderId;
    private final String address;
    private final String zipCode;

    public OrderAddressResponseDto(Order order) {
        this.orderId = order.getId();
        this.address = order.getAddress();
        this.zipCode = order.getZipCode();
    }



}
