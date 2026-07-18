package com.team3.coffee_order.dto.menu;

import lombok.Getter;

@Getter
public class MenuUpdateRequestDto {

    private String name;
    private Integer price;
    private String description;
}
