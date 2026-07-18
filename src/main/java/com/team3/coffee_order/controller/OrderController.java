package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.OrderGetResponse;
import com.team3.coffee_order.service.OrderService;
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

    // TODO: delete
}