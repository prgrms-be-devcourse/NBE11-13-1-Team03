package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.OrderCreateRequest;
import com.team3.coffee_order.dto.OrderCreateResponse;
import com.team3.coffee_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // TODO: create
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.create(request);
    }

    // TODO: read

    // TODO: update

    // TODO: delete
}