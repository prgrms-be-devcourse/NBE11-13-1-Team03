package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // 메뉴가 soft delete 되어도 DB 테이블에서는 이름을 조회할 수 있으므로,
    // 메뉴명으로 주문 id를 찾을 때는 native query로 직접 조회한다.
    @Query(value = """
            select distinct oi.order_id
            from order_items oi
            join menus m on oi.menu_id = m.id
            where m.name = :menuName
            """, nativeQuery = true)
    List<Long> findOrderIdsByMenuNameIncludingDeleted(@Param("menuName") String menuName);

    // 주문 응답에서 메뉴 엔티티를 직접 사용하지 않고,
    // orderItem 기준으로 메뉴 id/이름을 다시 조회해 응답 생성에 사용한다.
    @Query(value = """
            select oi.id as orderItemId, m.id as menuId, m.name as menuName
            from order_items oi
            join menus m on oi.menu_id = m.id
            where oi.id in (:orderItemIds)
            """, nativeQuery = true)
    List<OrderItemMenuInfoProjection> findMenuInfosByOrderItemIds(@Param("orderItemIds") List<Long> orderItemIds);


}