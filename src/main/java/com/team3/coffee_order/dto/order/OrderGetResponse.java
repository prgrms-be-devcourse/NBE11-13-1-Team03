package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.dto.orderItem.OrderItemGetResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderGetResponse {
    private Long orderId;
    private String email;
    private String address;
    private String zipCode;
    private String orderDate;
    private String status;
    private int totalPrice;
    private List<OrderItemGetResponse> items;
}
