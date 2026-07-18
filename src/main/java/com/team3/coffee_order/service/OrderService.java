package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.order.OrderStatusResponseDto;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // TODO: create

    // TODO: read

    // TODO: update
    @Transactional
    public ResponseEntity<OrderStatusResponseDto> updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required.");
        }
        if (request.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order status is required.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        order.updateStatus(request.getStatus());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OrderStatusResponseDto(order));
    }

    // TODO: delete
}
