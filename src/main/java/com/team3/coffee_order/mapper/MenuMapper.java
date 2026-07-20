package com.team3.coffee_order.mapper;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.dto.MenuGetResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public MenuGetResponse toMenuGetResponse(Menu menu) {
        return new MenuGetResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription()
        );
    }
}
