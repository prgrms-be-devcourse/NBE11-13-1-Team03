package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            where c.email = :email
            """)
    List<Order> findAllByCustomerEmail(@Param("email") String email);

    // 관리자 주문 조회용 쿼리.
    // customer와 orderItems를 fetch join으로 함께 조회해 주문 응답 생성 시 N+1 문제를 줄인다.
    // menuName 조건은 service에서 삭제된 메뉴까지 포함한 orderIds로 먼저 변환하고, repository에서는 해당 주문 id 목록만 조건으로 사용한다.
    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            where (:email is null or c.email = :email)
            and (:status is null or o.status = :status)
            and (:orderDate is null or o.orderDate = :orderDate)
            and (:useOrderIds = false  or o.id in :orderIds)
            """)
    List<Order> searchOrders(
            @Param("email") String email,
            @Param("status") OrderStatus status,
            @Param("orderDate") LocalDate orderDate,
            @Param("useOrderIds") boolean useOrderIds,
            @Param("orderIds") List<Long> orderIds
    );

    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            where o.id = :orderId
            """)
    Optional<Order> findDetailById(@Param("orderId") Long orderId);

    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            where o.id = :orderId
            and c.email = :email
            """)
    Optional<Order> findDetailByIdAndCustomerEmail(
            @Param("orderId") Long orderId,
            @Param("email") String email
    );

    // createdAt 시간 범위와 status 조건을 함께 사용해 배송 예정 주문만 조회한다.
    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            where o.createdAt >= :startDateTime
            and o.createdAt < :endDateTime
            and o.status = :status
            """)
    List<Order> findShippingTargetOrders(
            @Param("startDateTime") LocalDateTime startDate,
            @Param("endDateTime") LocalDateTime endDate,
            @Param("status") OrderStatus status
    );
}