package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.order.OrderCreateRequest;
import com.team3.coffee_order.dto.order.OrderCreateResponse;
import com.team3.coffee_order.dto.order.*;
import com.team3.coffee_order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // TODO: create
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @Valid
            @RequestBody
            OrderCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.create(request));
    }

    // TODO: read
    @GetMapping
    public ResponseEntity<List<OrderGetResponse>> getOrderByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(email));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponse> getOrderById(
            @PathVariable Long orderId,
            @RequestParam String email
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrder(orderId, email));
    }

    // TODO: update
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String email
    ) {
        return orderService.cancelOrder(orderId, email);
    }

    @PatchMapping("/{orderId}/shipping-address")
    public ResponseEntity<OrderAddressResponse> updateShippingAddress(
            @PathVariable Long orderId,
            @RequestParam String email,
            @Valid @RequestBody OrderAddressUpdateRequest request
    ) {
        return orderService.updateShippingAddress(orderId, email, request);
    }

    // TODO: delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
