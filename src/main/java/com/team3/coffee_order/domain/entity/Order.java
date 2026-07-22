package com.team3.coffee_order.domain.entity;

import com.team3.coffee_order.exception.InvalidArgumentException;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
// import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted = true, deleted_at = NOW() WHERE id = ?")
// 팀 논의 후 제거: 조회 시 deleted=false 조건은 각 리포지토리 메서드에서 명시적으로 처리한다.
// @SQLRestriction("deleted = false")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Order() {
    }

    public Order(
            Customer customer,
            OrderStatus status,
            String address,
            String zipCode
    ) {
        this.customer = customer;
        this.status = status;
        this.address = address;
        this.zipCode = zipCode;
        this.createdAt = LocalDateTime.now();
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }

    // 호출 경로와 관계없이 동일한 주문 상태 변경 규칙을 보장하기 위해 엔티티 내부에서 검증
    public void updateStatus(OrderStatus nextStatus) {

        if (!status.canChangeTo(nextStatus)) {
            throw new InvalidArgumentException(
                    status + "에서 " + nextStatus + "로 변경할 수 없습니다."
            );
        }

        this.status = nextStatus;
    }

    // 호출 경로와 관계없이 동일한 배송지 변경 규칙을 보장하기 위해 엔티티 내부에서 검증
    public void updateShippingAddress(String address, String zipCode) {
        if (status != OrderStatus.ORDERED)
            throw new InvalidArgumentException("주문이 취소되었거나 배송이 시작된 이후에는 배송지를 변경할 수 없습니다.");

        this.address = address;
        this.zipCode = zipCode;
    }
}