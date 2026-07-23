package com.team3.coffee_order.dto.menu;

import com.team3.coffee_order.domain.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MenuResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer stock;
    private final String description;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.stock = menu.getStock();
        this.description = menu.getDescription();
        this.imageUrl = menu.getImageUrl();
        this.createdAt = menu.getCreatedAt();
        this.updatedAt = menu.getUpdatedAt();
    }
}
