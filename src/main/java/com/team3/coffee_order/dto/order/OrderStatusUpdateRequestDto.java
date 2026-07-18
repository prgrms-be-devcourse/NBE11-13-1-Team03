package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.domain.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {

    private OrderStatus status;
}
