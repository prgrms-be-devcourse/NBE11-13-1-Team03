package com.team3.coffee_order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String address;

    @NotEmpty
    private String zipCode;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;
}
