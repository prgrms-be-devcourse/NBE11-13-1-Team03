package com.team3.coffee_order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String description;
}
