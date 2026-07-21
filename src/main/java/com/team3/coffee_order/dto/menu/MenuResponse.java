package com.team3.coffee_order.dto.menu;

import com.team3.coffee_order.domain.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MenuResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
        this.createdAt = menu.getCreatedAt();
        this.updatedAt = menu.getUpdatedAt();
    }
}
