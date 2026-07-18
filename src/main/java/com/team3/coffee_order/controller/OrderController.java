package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.order.OrderStatusResponseDto;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequestDto;
import com.team3.coffee_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // TODO: create

    // TODO: read

    // TODO: update
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto request
    ) {
        return orderService.updateOrderStatus(orderId, request);
    }

    // TODO: delete
}
