package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.dto.orderItem.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String zipCode;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;
}
