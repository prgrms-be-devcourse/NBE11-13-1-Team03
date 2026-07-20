package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.*;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.OrderCreateRequest;
import com.team3.coffee_order.dto.OrderCreateResponse;
import com.team3.coffee_order.dto.OrderItemRequest;
import com.team3.coffee_order.dto.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerService customerService;
    private final MenuService menuService;

    // TODO: create
    @Transactional
    public ResponseEntity<OrderCreateResponse> create(OrderCreateRequest request) {
        List<OrderItemRequest> items = request.getItems();

        Customer customer = customerService.findOrCreateByEmail(request.getEmail());

        LocalDate orderDate = calculateOrderDate(LocalDateTime.now());

        Order order = orderRepository.findByCustomerAndOrderDate(customer, orderDate)
                .orElseGet(() -> orderRepository.save(
                        new Order(customer, orderDate, OrderStatus.ORDERED, request.getAddress(), request.getZipCode())
                ));

        for (OrderItemRequest item : items) {
            Menu menu = menuService.getMenuEntityById(item.getMenuId());
            OrderItem orderItem = new OrderItem(order, menu, item.getQuantity(), menu.getPrice());
            order.addOrderItem(orderItem);
            orderItemRepository.save(orderItem);
        }

        int totalAmount = order.getOrderItems().stream()
                .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(oi -> OrderItemResponse.builder()
                        .menuName(oi.getMenu().getName())
                        .quantity(oi.getQuantity())
                        .unitPrice(oi.getUnitPrice())
                        .build())
                .toList();

        OrderCreateResponse response = OrderCreateResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .totalAmount(totalAmount)
                .items(itemResponses)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private LocalDate calculateOrderDate(LocalDateTime now) {
        LocalTime cutoff = LocalTime.of(14, 0);
        return now.toLocalTime().isBefore(cutoff)
                ? now.toLocalDate()
                : now.toLocalDate().plusDays(1);
    }

    // TODO: read

    // TODO: update

    // TODO: delete
}