package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderItem;
import com.team3.coffee_order.domain.entity.OrderStatus;
import com.team3.coffee_order.domain.repository.OrderItemMenuInfoProjection;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.order.OrderGetResponse;
import com.team3.coffee_order.dto.order.OrderStatusResponseDto;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequestDto;
import com.team3.coffee_order.exception.*;
import com.team3.coffee_order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            throw new InvalidEmailException("이메일은 비어 있을 수 없습니다.");
        }

        String trimmedEmail = email.trim();

        List<Order> orders = orderRepository.findAllByCustomerEmail(trimmedEmail).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .toList();

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(orders);

        return orders.stream()
                .map(order -> orderMapper.toOrderGetResponse(order, menuInfoMap))
                .toList();
    }

    // 문자열 조건은 trim 후 null로 정리하고, status/date는 타입 변환한다.
    // menuName이 있으면 삭제된 메뉴도 검색할 수 있도록 먼저 주문 id 목록을 구한 뒤 주문을 조회한다.
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getOrders(String email, String status, String orderDate, String menuName) {
        String trimmedEmail = (email == null || email.isBlank()) ? null : email.trim();
        String trimmedMenuName = (menuName == null || menuName.isBlank()) ? null : menuName.trim();

        OrderStatus parseOrderStatus = parseOrderStatus(status);
        LocalDate parsedOrderDate = parseOrderDate(orderDate);

        // menuName 필터가 있을 때만 주문 id 목록 조건을 사용한다.
        // 기본값 -1L은 빈 IN() 조건으로 인한 쿼리 오류를 피하기 위한 안전장치다.
        boolean useOrderIds = false;
        List<Long> orderIds = List.of(-1L);

        if (trimmedMenuName != null) {
            orderIds = orderItemRepository.findOrderIdsByMenuNameIncludingDeleted(trimmedMenuName);

            if (orderIds.isEmpty()) {
                return List.of();
            }

            useOrderIds = true;
        }

        List<Order> orders = orderRepository.searchOrders(trimmedEmail, parseOrderStatus, parsedOrderDate, useOrderIds, orderIds).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .toList();

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(orders);

        return orders.stream()
                .map(order -> orderMapper.toOrderGetResponse(order, menuInfoMap))
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderGetResponse getOrderById(Long orderId) {
        Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("해당하는 주문이 존재하지 않습니다. id = "+orderId));

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(List.of(order));

        return orderMapper.toOrderGetResponse(order, menuInfoMap);
    }

    // 고객 주문 단건 조회는 email과 orderId가 모두 일치하는 경우만 반환해 다른 고객의 주문이 노출되지 않도록 한다.
    @Transactional(readOnly = true)
    public OrderGetResponse getOrderByIdAndEmail(Long orderId, String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("이메일은 비어 있을 수 없습니다.");
        }

        String trimmedEmail = email.trim();

        Order order = orderRepository.findDetailByIdAndCustomerEmail(orderId, trimmedEmail)
                .orElseThrow(() -> new OrderNotFoundException("해당하는 주문이 존재하지 않습니다. id = "+orderId));

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(List.of(order));

        return orderMapper.toOrderGetResponse(order, menuInfoMap);
    }

    // 배송 대상 주문은 조회 기준일의 전날 14:00 이상, 당일 14:00 미만에 생성된 주문만 조회한다.
    // 이미 배송 처리가 시작된 주문은 제외하기 위해 ORDERED 상태만 조회한다.
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getShippingTargetOrders(String date) {
        if (date == null || date.isBlank()) {
            throw new InvalidOrderDateException("날짜 형식이 올바르지 않습니다 (yyyy-MM-dd). orderDate=" + date);
        }

        LocalDate shippingDate = parseOrderDate(date);

        LocalDateTime startDate = shippingDate.minusDays(1).atTime(14, 0);
        LocalDateTime endDate = shippingDate.atTime(14, 0);

        List<Order> orders = orderRepository.findShippingTargetOrders(startDate, endDate, OrderStatus.ORDERED).stream()
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .toList();

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(orders);

        return orders.stream()
                .map(order -> orderMapper.toOrderGetResponse(order, menuInfoMap))
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
            throw new InvalidOrderStatusException("잘못된 주문 상태입니다. status=" + status);
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
            throw new InvalidOrderDateException("날짜 형식이 올바르지 않습니다 (yyyy-MM-dd). orderDate=" + orderDate);
        }
    }

    // 주문 응답에서는 메뉴 엔티티를 직접 참조하지 않으므로,
    // 조회된 모든 orderItemId 기준으로 메뉴 id/이름을 Map 형태로 만든다.
    private Map<Long, OrderItemMenuInfoProjection> getMenuInfoMap(List<Order> orders) {
        List<Long> orderItemIds = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getId)
                .toList();

        if (orderItemIds.isEmpty()) {
            return Map.of();
        }

        return orderItemRepository.findMenuInfosByOrderItemIds(orderItemIds).stream()
                .collect(Collectors.toMap(
                        OrderItemMenuInfoProjection::getOrderItemId,
                        Function.identity()
                ));
    }




    // TODO: update
    @Transactional
    public ResponseEntity<OrderStatusResponseDto> updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        // 검증된 요청 값으로 주문을 조회하고, 주문 상태만 변경해 결과를 반환한다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        order.updateStatus(request.getStatus());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OrderStatusResponseDto(order));
    }

    // TODO: delete
    public ResponseEntity<Void> deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("해당하는 주문을 찾을 수 없습니다. id = "+id));

        orderRepository.delete(order);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
