package com.team3.coffee_order.controller;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.dto.demo.DemoDecreaseStockRequest;
import com.team3.coffee_order.dto.demo.DemoStockResponse;
import com.team3.coffee_order.service.DemoConcurrencyService;
import com.team3.coffee_order.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 발표용 동시성 비교 Controller
@RestController
@RequestMapping("/api/demo/concurrency")
@RequiredArgsConstructor
public class DemoConcurrencyController {

    private final MenuService menuService;
    private final DemoConcurrencyService demoConcurrencyService;

    @PostMapping("/menus/{menuId}/decrease-with-lock")
    public ResponseEntity<DemoStockResponse> decreaseWithLock(
            @PathVariable Long menuId,
            @Valid @RequestBody DemoDecreaseStockRequest request
    ) {
        Menu menu = menuService.decreaseStockForOrder(menuId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(new DemoStockResponse(menu.getId(), menu.getStock()));
    }

    @PostMapping("/menus/{menuId}/decrease-without-lock")
    public ResponseEntity<DemoStockResponse> decreaseWithoutLock(
            @PathVariable Long menuId,
            @Valid @RequestBody DemoDecreaseStockRequest request
    ) {
        int stock = demoConcurrencyService.decreaseStockWithoutLock(menuId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(new DemoStockResponse(menuId, stock));
    }
}
