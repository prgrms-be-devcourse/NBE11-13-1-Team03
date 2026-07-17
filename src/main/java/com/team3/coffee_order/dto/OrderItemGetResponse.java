package com.team3.coffee_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemGetResponse {
    private Long menuId;
    private String menuName;
    private int quantity;
    private int unitPrice;
}
