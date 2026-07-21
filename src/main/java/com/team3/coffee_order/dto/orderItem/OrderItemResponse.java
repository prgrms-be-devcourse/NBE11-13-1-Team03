package com.team3.coffee_order.dto.orderItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private String menuName;
    private Integer quantity;
    private Integer unitPrice;
}
