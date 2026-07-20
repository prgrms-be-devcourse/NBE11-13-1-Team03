package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.OrderCreateRequest;
import com.team3.coffee_order.dto.OrderCreateResponse;
import com.team3.coffee_order.dto.order.OrderGetResponse;
import com.team3.coffee_order.dto.order.OrderStatusResponseDto;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequestDto;
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
        return orderService.create(request);
    }

    // TODO: read
    @GetMapping
    public ResponseEntity<List<OrderGetResponse>> getOrderByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByEmail(email));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponse> getOrderById(
            @PathVariable Long orderId,
            @RequestParam String email
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderByIdAndEmail(orderId, email));
    }

    // TODO: update
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto request
    ) {
        return orderService.updateOrderStatus(orderId, request);
    }

    // TODO: delete

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        return orderService.deleteOrder(id);
    }
}
