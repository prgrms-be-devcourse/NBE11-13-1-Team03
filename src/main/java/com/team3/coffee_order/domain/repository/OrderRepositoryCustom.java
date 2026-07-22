package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    List<Order> findAllByCustomerEmail(String email);

    List<Order> searchOrders(String email, OrderStatus status, boolean useOrderIds, List<Long> orderIds);

    Optional<Order> findDetailById(Long orderId);

    Optional<Order> findDetailByIdAndCustomerEmail(Long orderId, String email);

    List<Long> findIdsByCreatedAtBetweenAndStatus(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus status);

    int bulkUpdateStatus(List<Long> ids, OrderStatus status);
}
