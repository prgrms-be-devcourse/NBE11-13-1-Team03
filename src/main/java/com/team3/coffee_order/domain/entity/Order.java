package com.team3.coffee_order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")  // order는 예약어라 테이블명을 orders로 사용
@SQLDelete(sql = "UPDATE orders SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private LocalDate orderDate;

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

    protected Order() {}

    public Order(Customer customer, LocalDate orderDate, OrderStatus status, String address, String zipCode) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.address = address;
        this.zipCode = zipCode;
        this.createdAt = LocalDateTime.now();
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }
}