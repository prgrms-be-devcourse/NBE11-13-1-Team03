package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerAndOrderDate(Customer customer, LocalDate orderDate);
  
    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            left join fetch oi.menu
            where c.email = :email
            """)
    List<Order> findAllByCustomerEmail(@Param("email") String email);

    // 관리자 주문 조회용 쿼리.
    // customer, orderItems, menu를 fetch join으로 함께 조회해 N+1 문제를 줄인다.
    // menuName은 exists 서브쿼리로 검사해서, 특정 메뉴로 필터링해도 주문의 전체 items와 totalPrice는 유지되도록 한다.
    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            left join fetch oi.menu
            where (:email is null or c.email = :email)
            and (:status is null or o.status = :status)
            and (:orderDate is null or o.orderDate = :orderDate)
            and (
                    :menuName is null or exists (
                        select 1
                        from OrderItem oi2
                        join oi2.menu m2
                        where oi2.order = o
                        and m2.name = :menuName
                    )
                )
            """)
    List<Order> searchOrders(
            @Param("email") String email,
            @Param("status") OrderStatus status,
            @Param("orderDate") LocalDate orderDate,
            @Param("menuName") String menuName
    );

    @Query("""
            SELECT distinct o from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            left join fetch oi.menu
            where o.id = :orderId
            """)
    Optional<Order> findDetailById(@Param("orderId") Long orderId);


    @Query("""
            SELECT distinct o
            from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            left join fetch oi.menu
            where o.id = :orderId
            and c.email = :email
            """)
    Optional<Order> findDetailByIdAndCustomerEmail(
            @Param("orderId") Long orderId,
            @Param("email") String email
    );

    // createdAt 시간 범위와 status 조건을 함께 사용해 배송 예정 주문만 조회한다.
    @Query("""
            SELECT distinct o
            from Order o
            join fetch o.customer c
            left join fetch o.orderItems oi
            left join fetch oi.menu
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