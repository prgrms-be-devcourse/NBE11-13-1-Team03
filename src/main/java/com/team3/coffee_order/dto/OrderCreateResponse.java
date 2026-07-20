package com.team3.coffee_order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderCreateResponse {

    private Long orderId;
    private LocalDate orderDate;
    private String status;
    private Integer totalAmount;
    private List<OrderItemResponse> items;
}
