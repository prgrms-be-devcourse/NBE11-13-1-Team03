package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.*;
import com.team3.coffee_order.domain.repository.OrderItemMenuInfoProjection;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.order.OrderCreateRequest;
import com.team3.coffee_order.dto.order.OrderCreateResponse;
import com.team3.coffee_order.dto.orderItem.OrderItemRequest;
import com.team3.coffee_order.dto.order.*;
import com.team3.coffee_order.exception.*;
import com.team3.coffee_order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final OrderMapper orderMapper;


    // TODO: create
    @Transactional
    public OrderCreateResponse create(OrderCreateRequest request) {
        List<OrderItemRequest> items = request.getItems();

        Customer customer = customerService.findOrCreateByEmail(request.getEmail());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowEnd = now.toLocalTime().isBefore(LocalTime.of(14, 0))
                ? now.toLocalDate().atTime(14, 0)
                : now.toLocalDate().plusDays(1).atTime(14, 0);
        LocalDateTime windowStart = windowEnd.minusDays(1);

        Order order = orderRepository.findByCustomerAndCreatedAtBetween(customer, windowStart, windowEnd)
                .orElseGet(() -> orderRepository.save(
                        new Order(customer, OrderStatus.ORDERED, request.getAddress(), request.getZipCode())));

        for (OrderItemRequest item : items) {
            Menu menu = menuService.getMenuEntity(item.getMenuId());
            OrderItem orderItem = new OrderItem(order, menu, item.getQuantity(), menu.getPrice());
            order.addOrderItem(orderItem);
            orderItemRepository.save(orderItem);
        }

        return orderMapper.toOrderCreateResponse(order);
    }

    // TODO: read
    public List<OrderGetResponse> getOrder(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidArgumentException("이메일은 비어 있을 수 없습니다.");
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

    // 문자열 조건은 trim 후 null로 정리하고, status는 타입 변환한다.
    // menuName이 있으면 삭제된 메뉴도 검색할 수 있도록 먼저 주문 id 목록을 구한 뒤 주문을 조회한다.
    @Transactional(readOnly = true)
    public List<OrderGetResponse> getOrders(String email, String status, String menuName) {
        String trimmedEmail = (email == null || email.isBlank()) ? null : email.trim();
        String trimmedMenuName = (menuName == null || menuName.isBlank()) ? null : menuName.trim();

        OrderStatus parseOrderStatus = parseOrderStatus(status);

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

        List<Order> orders = orderRepository.searchOrders(trimmedEmail, parseOrderStatus, useOrderIds, orderIds).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .toList();

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(orders);

        return orders.stream()
                .map(order -> orderMapper.toOrderGetResponse(order, menuInfoMap))
                .toList();
    }

    public OrderGetResponse getOrderAdmin(Long orderId) {
        Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(() -> new NotFoundException("주문이 존재하지 않습니다. orderId=" + orderId));

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(List.of(order));

        return orderMapper.toOrderGetResponse(order, menuInfoMap);
    }

    // 고객 주문 단건 조회는 email과 orderId가 모두 일치하는 경우만 반환해 다른 고객의 주문이 노출되지 않도록 한다.
    public OrderGetResponse getOrder(Long orderId, String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidArgumentException("이메일은 비어 있을 수 없습니다.");
        }

        String trimmedEmail = email.trim();

        Order order = orderRepository.findDetailByIdAndCustomerEmail(orderId, trimmedEmail)
                .orElseThrow(() -> new NotFoundException("주문이 존재하지 않습니다. orderId=" + orderId));

        Map<Long, OrderItemMenuInfoProjection> menuInfoMap = getMenuInfoMap(List.of(order));

        return orderMapper.toOrderGetResponse(order, menuInfoMap);
    }


    // status 문자열을 OrderStatus enum으로 변환한다. 잘못된 값이 들어오면 400 응답
    private OrderStatus parseOrderStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return OrderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException("잘못된 주문 상태입니다. status=" + status);
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
    public OrderStatusResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        // 검증된 요청 값으로 주문을 조회하고, 주문 상태만 변경해 결과를 반환한다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. orderId="+orderId));

        order.updateStatus(request.getStatus());

        return new OrderStatusResponse(order);
    }

    // 배송 시작 전인 ORDERED 상태에서만 고객이 주문을 취소할 수 있다.
    @Transactional
    public OrderStatusResponse cancelOrder(Long orderId, String email) {

        if (email == null || email.isBlank()) {
            throw new InvalidArgumentException(
                    "이메일은 비어 있을 수 없습니다."
            );
        }

        String trimmedEmail = email.trim();

        Order order = orderRepository
                .findDetailByIdAndCustomerEmail(orderId, trimmedEmail)
                .orElseThrow(() ->
                        new NotFoundException(
                                "주문이 존재하지 않습니다. orderId=" + orderId
                        )
                );

        order.updateStatus(OrderStatus.CANCELED);

        return new OrderStatusResponse(order);
    }

    // 고객 주문의 배송지를 배송 시작 전에 변경한다.
    @Transactional
    public OrderAddressResponse updateShippingAddress(
            Long orderId,
            String email,
            OrderAddressUpdateRequest request
    ) {
        if (email == null || email.isBlank())
            throw new InvalidArgumentException("이메일은 비어 있을 수 없습니다.");

        String trimmedEmail = email.trim();

        Order order = orderRepository
                .findDetailByIdAndCustomerEmail(orderId, trimmedEmail)
                .orElseThrow(() -> new NotFoundException(
                        "주문이 존재하지 않습니다. orderId=" + orderId
                ));

        order.updateShippingAddress(
                request.getAddress().trim(),
                request.getZipCode().trim()
        );

        return new OrderAddressResponse(order);
    }


    // TODO: delete
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. orderId=" + orderId));

        orderRepository.delete(order);
    }
}
