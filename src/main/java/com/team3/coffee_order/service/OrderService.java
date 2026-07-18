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
import java.time.LocalDateTime;
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

    // 고객 주문 단건 조회는 email과 orderId가 모두 일치하는 경우만 반환해 다른 고객의 주문이 노출되지 않도록 한다.
    @Transactional(readOnly = true)
    public OrderGetResponse getOrderByIdAndEmail(Long orderId, String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException();
        }

        String trimmedEmail = email.trim();

        Order order = orderRepository.findDetailByIdAndCustomerEmail(orderId, trimmedEmail)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderMapper.toOrderGetResponse(order);
    }

    // 배송 대상 주문은 조회 기준일의 전날 14:00 이상, 당일 14:00 미만에 생성된 주문만 조회한다.
    // 이미 배송 처리가 시작된 주문은 제외하기 위해 ORDERED 상태만 조회한다.
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getShippingTargetOrders(String date) {
        if (date == null || date.isBlank()) {
            throw new InvalidOrderDateException(date);
        }

        LocalDate shippingDate = parseOrderDate(date);

        LocalDateTime startDate = shippingDate.minusDays(1).atTime(14, 0);
        LocalDateTime endDate = shippingDate.atTime(14, 0);

        return orderRepository.findShippingTargetOrders(startDate, endDate, OrderStatus.ORDERED).stream()
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .map(orderMapper::toOrderGetResponse)
                .toList();
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