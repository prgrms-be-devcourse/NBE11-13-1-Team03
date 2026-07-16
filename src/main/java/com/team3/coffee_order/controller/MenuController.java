package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.MenuGetResponse;
import com.team3.coffee_order.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // TODO: create

    // TODO: read
    @GetMapping
    public ResponseEntity<List<MenuGetResponse>> getMenus() {
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenus());
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuGetResponse> getMenu(@PathVariable("menuId") long menuId) {
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenu(menuId));
    }

    // TODO: update

    // TODO: delete
}