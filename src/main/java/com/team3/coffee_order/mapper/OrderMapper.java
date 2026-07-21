package com.team3.coffee_order.mapper;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderItem;
import com.team3.coffee_order.domain.repository.OrderItemMenuInfoProjection;
import com.team3.coffee_order.dto.order.OrderCreateResponse;
import com.team3.coffee_order.dto.order.OrderGetResponse;
import com.team3.coffee_order.dto.orderItem.OrderItemGetResponse;
import com.team3.coffee_order.dto.orderItem.OrderItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderMapper {
    public OrderGetResponse toOrderGetResponse(Order order, Map<Long, OrderItemMenuInfoProjection> menuInfoMap) {
        List<OrderItemGetResponse> items = order.getOrderItems().stream()
                .map(orderItem -> toOrderItemGetResponse(orderItem, menuInfoMap))
                .toList();

        int totalPrice = order.getOrderItems().stream()
                .mapToInt(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return new OrderGetResponse(
                order.getId(),
                order.getCustomer().getEmail(),
                order.getAddress(),
                order.getZipCode(),
                order.getStatus().name(),
                totalPrice,
                items
        );
    }

    // 주문 항목의 메뉴 정보는 menuInfoMap 기준으로 응답을 만든다.
    // soft delete 된 메뉴가 있어도 orderItemId 기준 조회 결과로 menuId, menuName을 복구할 수 있다.
    public OrderItemGetResponse toOrderItemGetResponse(OrderItem orderItem, Map<Long, OrderItemMenuInfoProjection> menuInfoMap) {
        OrderItemMenuInfoProjection menuInfo = menuInfoMap.get(orderItem.getId());

        Long menuId = (menuInfo != null) ? menuInfo.getMenuId() : null;
        String menuName = (menuInfo != null) ? menuInfo.getMenuName() : "삭제된 메뉴";

        return new OrderItemGetResponse(
                menuId,
                menuName,
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }

    public OrderCreateResponse toOrderCreateResponse(Order order) {
        int totalAmount = order.getOrderItems().stream()
                .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .menuName(item.getMenu().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .toList();

        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .totalAmount(totalAmount)
                .items(itemResponses)
                .build();
    }
}
