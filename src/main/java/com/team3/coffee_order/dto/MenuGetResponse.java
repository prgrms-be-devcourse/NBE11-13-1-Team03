package com.team3.coffee_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuGetResponse {
    private Long menuId;
    private String name;
    private int price;
    private String description;
}
