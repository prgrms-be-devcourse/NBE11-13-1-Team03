package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.order.OrderGetResponse;
import com.team3.coffee_order.dto.order.OrderStatusResponse;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequest;
import com.team3.coffee_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderGetResponse>> getOrders(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String menuName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders(email, status, menuName));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponse> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderAdmin(orderId));
    }

    // update
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        return orderService.updateOrderStatus(orderId, request);
    }
}
