package com.team3.coffee_order.controller;

import com.team3.coffee_order.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // TODO: create

    // TODO: read

    // TODO: update

    // TODO: delete
}