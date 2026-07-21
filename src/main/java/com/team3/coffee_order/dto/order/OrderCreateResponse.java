package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.dto.orderItem.OrderItemResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderCreateResponse {

    private Long orderId;
    private String status;
    private Integer totalAmount;
    private List<OrderItemResponse> items;
}
