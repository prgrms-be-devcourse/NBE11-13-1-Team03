package com.team3.coffee_order.dto.demo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DemoDecreaseStockRequest {

    @NotNull
    @Min(1)
    private Integer quantity;
}
