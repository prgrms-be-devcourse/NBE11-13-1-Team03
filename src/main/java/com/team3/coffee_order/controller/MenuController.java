package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.menu.MenuResponseDto;
import com.team3.coffee_order.dto.menu.MenuUpdateRequestDto;
import com.team3.coffee_order.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // TODO: create

    // TODO: read

    // TODO: update
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto request
    ) {
        return menuService.updateMenu(menuId, request);
    }

    // TODO: delete
}
