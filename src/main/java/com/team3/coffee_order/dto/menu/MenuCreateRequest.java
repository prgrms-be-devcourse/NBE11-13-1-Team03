package com.team3.coffee_order.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고는 필수입니다.")
    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    private String description;
}
