package com.team3.coffee_order.domain.entity;

import com.team3.coffee_order.exception.InvalidArgumentException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
// import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "menus")
@SQLDelete(sql = "UPDATE menus SET deleted = true, deleted_at = NOW() WHERE id = ?")
// 팀 논의 후 제거: 조회 시 deleted=false 조건은 각 리포지토리 메서드에서 명시적으로 처리한다.
// @SQLRestriction("deleted = false")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    protected Menu() {}

    public Menu(String name, Integer price, Integer stock, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, Integer price, Integer stock, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    //상품 재고 차감
    public void decreaseStock(int quantity){
        if(this.stock<quantity){
            throw new InvalidArgumentException("재고 부족: 재고 "+this.stock+"요청 "+quantity);
        }
        this.stock-=quantity;
        this.updatedAt = LocalDateTime.now();
    }

    //상품 재고 복구
    public void increaseStock(int quantity){
        if(quantity <= 0){
            throw new InvalidArgumentException("복구 수량은 1개 이상이어야 합니다.");
        }
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
