package com.team3.coffee_order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_items")
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer unitPrice;

    protected OrderItem() {}

    public OrderItem(Order order, Menu menu, Integer quantity, Integer unitPrice) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}