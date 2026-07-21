package com.team3.coffee_order.dto.order;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderAddressUpdateRequestDto {
    @NotBlank
    private String address;
    @NotBlank
    private String zipCode;
}
