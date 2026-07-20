package com.team3.coffee_order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {

    @NotNull
    private Long menuId;

    @Min(1)
    private Integer quantity;
}
