package com.team3.coffee_order.dto.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemoStockResponse {
    private Long menuId;
    private int stock;
}
