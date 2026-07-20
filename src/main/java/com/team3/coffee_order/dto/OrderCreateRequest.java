package com.team3.coffee_order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String zipCode;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
