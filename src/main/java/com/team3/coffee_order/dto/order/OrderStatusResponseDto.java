package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusResponseDto {

    private final Long id;
    private final OrderStatus status;

    public OrderStatusResponseDto(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
    }
}
