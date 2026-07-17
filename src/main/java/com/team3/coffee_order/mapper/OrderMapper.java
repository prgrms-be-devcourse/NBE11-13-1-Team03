package com.team3.coffee_order.mapper;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderItem;
import com.team3.coffee_order.dto.OrderGetResponse;
import com.team3.coffee_order.dto.OrderItemGetResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderGetResponse toOrderGetResponse(Order order) {
        List<OrderItemGetResponse> items = order.getOrderItems().stream()
                .map(this::toOrderItemGetResponse)
                .toList();

        int totalPrice = order.getOrderItems().stream()
                .mapToInt(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return new OrderGetResponse(
                order.getId(),
                order.getCustomer().getEmail(),
                order.getAddress(),
                order.getZipCode(),
                order.getOrderDate().toString(),
                order.getStatus().name(),
                totalPrice,
                items
        );
    }

    public OrderItemGetResponse toOrderItemGetResponse(OrderItem orderItem) {
        return new OrderItemGetResponse(
                orderItem.getMenu().getId(),
                orderItem.getMenu().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }
}
