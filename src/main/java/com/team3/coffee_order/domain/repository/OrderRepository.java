package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerAndCreatedAtBetween(Customer customer, LocalDateTime start, LocalDateTime end);

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
            and (:useOrderIds = false  or o.id in :orderIds)
            """)
    List<Order> searchOrders(
            @Param("email") String email,
            @Param("status") OrderStatus status,
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

    // 배치 대상 id만 조회 (deleted=false는 Order 엔티티의 @SQLRestriction으로 자동 적용됨)
    @Query("""
        SELECT o.id
        FROM Order o
        WHERE o.createdAt >= :startDateTime
        AND o.createdAt < :endDateTime
        AND o.status = :status
        """)
    List<Long> findIdsByCreatedAtBetweenAndStatus(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("status") OrderStatus status
    );

    //OrderStatus 배치 처리로 변경 : bulk 연산으로 메모리 사용 최소화, DB 왕복 횟수 1회로 개선
    //bulk 연산은 영속성 컨텍스트를 거치지 않고 DB로 직접 SQL 날림 => clearAutomatically = true
    //=> 영속성 컨텍스트에 남은 변경 전 데이터 지우고, 새로 DB에서 읽어옴
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Order o set o.status = :status WHERE o.id IN :ids")
    int bulkUpdateStatus(@Param("ids") List<Long> ids, @Param("status") OrderStatus status);
}