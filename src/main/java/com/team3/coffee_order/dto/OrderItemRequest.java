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

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private Integer quantity;
}
