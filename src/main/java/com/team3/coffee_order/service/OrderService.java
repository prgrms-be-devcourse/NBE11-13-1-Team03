package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.OrderGetResponse;
import com.team3.coffee_order.exception.InvalidEmailException;
import com.team3.coffee_order.exception.InvalidOrderDateException;
import com.team3.coffee_order.exception.InvalidOrderStatusException;
import com.team3.coffee_order.exception.OrderNotFoundException;
import com.team3.coffee_order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    // TODO: create

    // TODO: read
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getOrderByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException();
        }

        String trimmedEmail = email.trim();

        return orderRepository.findAllByCustomerEmail(trimmedEmail).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(orderMapper::toOrderGetResponse)
                .toList();
    }

    // 문자열 조건은 trim 후 null로 정리하고, status/date는 타입 변환 후 repository에 전달한다.
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getOrders(String email, String status, String orderDate, String menuName) {
        String trimmedEmail = (email == null || email.isBlank()) ? null : email.trim();
        String trimmedMenuName = (menuName == null || menuName.isBlank()) ? null : menuName.trim();

        OrderStatus parseOrderStatus = parseOrderStatus(status);
        LocalDate parsedOrderDate = parseOrderDate(orderDate);

        return orderRepository.searchOrders(trimmedEmail, parseOrderStatus, parsedOrderDate, trimmedMenuName).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(orderMapper::toOrderGetResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderGetResponse getOrderById(Long orderId) {
        Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderMapper.toOrderGetResponse(order);
    }

    // status 문자열을 OrderStatus enum으로 변환한다. 잘못된 값이 들어오면 400 응답
    private OrderStatus parseOrderStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return OrderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException(status);
        }
    }

    // orderDate 문자열을 LocalDate로 변환한다. yyyy-MM-dd 형식이 아니면 400 응답
    private LocalDate parseOrderDate(String orderDate) {
        if (orderDate == null || orderDate.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(orderDate.trim());
        } catch (DateTimeParseException e) {
            throw new InvalidOrderDateException(orderDate);
        }
    }



    // TODO: update

    // TODO: delete
}