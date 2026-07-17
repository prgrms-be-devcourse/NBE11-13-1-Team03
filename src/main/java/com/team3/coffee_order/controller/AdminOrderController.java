package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.OrderGetResponse;
import com.team3.coffee_order.service.OrderService;
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
            @RequestParam(required = false) String orderDate,
            @RequestParam(required = false) String menuName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders(email, status, orderDate, menuName));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponse> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(orderId));
    }
}
