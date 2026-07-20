package com.team3.coffee_order.controller;

import com.team3.coffee_order.dto.menu.MenuGetResponse;
import com.team3.coffee_order.dto.menu.MenuResponseDto;
import com.team3.coffee_order.dto.menu.MenuUpdateRequestDto;
import com.team3.coffee_order.service.MenuService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto request
    ) {
        return menuService.updateMenu(menuId, request);
    }

    // TODO: delete
    @Operation(summary = "메뉴 삭제", description = "id로 메뉴를 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "메뉴 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "메뉴 삭제 실패 - 메뉴가 존재하지 않음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }
}
