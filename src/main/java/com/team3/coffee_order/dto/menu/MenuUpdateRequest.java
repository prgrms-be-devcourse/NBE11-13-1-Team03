package com.team3.coffee_order.dto.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class MenuUpdateRequest {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String name;

    @NotNull(message = "메뉴 가격은 필수입니다.")
    @PositiveOrZero(message = "메뉴 가격은 0원 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고는 필수입니다.")
    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    private String description;

    private String imageUrl;
}
