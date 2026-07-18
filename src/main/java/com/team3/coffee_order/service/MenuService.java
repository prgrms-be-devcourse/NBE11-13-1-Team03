package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.dto.menu.MenuResponseDto;
import com.team3.coffee_order.dto.menu.MenuUpdateRequestDto;
import com.team3.coffee_order.exception.MenuNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // TODO: create

    // TODO: read

    // TODO: update
    @Transactional
    public ResponseEntity<MenuResponseDto> updateMenu(Long menuId, MenuUpdateRequestDto request) {
        // 검증된 요청 값으로 메뉴를 조회하고, 엔티티 변경 메서드로 수정 결과를 반환한다.
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        menu.update(request.getName(), request.getPrice(), request.getDescription());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MenuResponseDto(menu));
    }

    // TODO: delete
}
