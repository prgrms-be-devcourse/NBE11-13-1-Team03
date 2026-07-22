package com.team3.coffee_order.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.entity.OrderStatus;
import com.team3.coffee_order.domain.entity.QCustomer;
import com.team3.coffee_order.domain.entity.QOrder;
import com.team3.coffee_order.domain.entity.QOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private static final QOrder order = QOrder.order;
    private static final QCustomer customer = QCustomer.customer;
    private static final QOrderItem orderItem = QOrderItem.orderItem;

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    //@SQLRestriction("deleted=false")를 사용 시, soft delete된 데이터를 가져오기 위해 네이티브 쿼리를 사용해야 했음
    //=> 가능한 컴파일 시에 에러를 잡기 위해서 @SQLRestriction을 제외하고 전부 queryDSL을 사용하기로 결졍
    //=> 왜 JPA derived method를 사용하지 않고 queryDSL을 사용하는가? : bulk 연산을 수행하는 bulkUpdateStatus를 표현하는 derived 네이밍 규칙이 없음
    //=> 최대한 코드의 일관성을 갖추기 위해 queryDSL로 통일


    @Override
    public List<Order> findAllByCustomerEmail(String email) {
        // Order 엔티티의 @SQLRestriction이 빠지면서 deleted 조건을 여기서 직접 챙겨야 한다.
        return queryFactory
                .selectFrom(order).distinct()
                .join(order.customer, customer).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .where(customer.email.eq(email), order.deleted.isFalse())
                .fetch();
    }


    @Override
    public List<Order> searchOrders(String email, OrderStatus status, boolean useOrderIds, List<Long> orderIds) {
        // Order 엔티티의 @SQLRestriction이 빠지면서 deleted 조건을 여기서 직접 챙겨야 한다.
        return queryFactory
                .selectFrom(order).distinct()
                .join(order.customer, customer).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .where(
                        emailEq(email),
                        statusEq(status),
                        useOrderIds ? order.id.in(orderIds) : null,
                        order.deleted.isFalse()
                )
                .fetch();
    }


    @Override
    public Optional<Order> findDetailById(Long orderId) {
        // Order 엔티티의 @SQLRestriction이 빠지면서 deleted 조건을 여기서 직접 챙겨야 한다.
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(order).distinct()
                        .join(order.customer, customer).fetchJoin()
                        .leftJoin(order.orderItems, orderItem).fetchJoin()
                        .where(order.id.eq(orderId), order.deleted.isFalse())
                        .fetchOne()
        );
    }


    @Override
    public Optional<Order> findDetailByIdAndCustomerEmail(Long orderId, String email) {
        // Order 엔티티의 @SQLRestriction이 빠지면서 deleted 조건을 여기서 직접 챙겨야 한다.
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(order).distinct()
                        .join(order.customer, customer).fetchJoin()
                        .leftJoin(order.orderItems, orderItem).fetchJoin()
                        .where(order.id.eq(orderId), customer.email.eq(email), order.deleted.isFalse())
                        .fetchOne()
        );
    }


    @Override
    public List<Long> findIdsByCreatedAtBetweenAndStatus(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus status) {
        // Order 엔티티의 @SQLRestriction이 빠지면서 deleted 조건을 여기서 직접 챙겨야 한다.
        // (이전 주석: deleted=false는 Order 엔티티의 @SQLRestriction으로 자동 적용됨 — 더 이상 사실이 아님)
        return queryFactory
                .select(order.id)
                .from(order)
                .where(
                        order.createdAt.goe(startDateTime),
                        order.createdAt.lt(endDateTime),
                        order.status.eq(status),
                        order.deleted.isFalse()
                )
                .fetch();
    }


    @Override
    public int bulkUpdateStatus(List<Long> ids, OrderStatus status) {
        // Order 엔티티의 @SQLRestriction이 빠지면서, 이미 삭제된 주문까지 상태가 바뀌지 않도록 deleted 조건을 직접 챙긴다.
        long updatedCount = queryFactory
                .update(order)
                .set(order.status, status)
                .where(order.id.in(ids), order.deleted.isFalse())
                .execute();

        em.clear();

        return (int) updatedCount;
    }

    private BooleanExpression emailEq(String email) {
        return email != null ? customer.email.eq(email) : null;
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }
}
