package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.dto.MenuGetResponse;
import com.team3.coffee_order.exception.MenuNotFoundException;
import com.team3.coffee_order.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.team3.coffee_order.dto.menu.MenuResponseDto;
import com.team3.coffee_order.dto.menu.MenuUpdateRequestDto;
import com.team3.coffee_order.exception.MenuNotFoundException;
import com.team3.coffee_order.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    // TODO: create

    // TODO: read
    public List<MenuGetResponse> getMenus() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(menuMapper::toMenuGetResponse)
                .toList();
    }

    public MenuGetResponse getMenu(long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        return menuMapper.toMenuGetResponse(menu);
    }

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
    @Transactional
    public ResponseEntity<Void> deleteMenu(Long id){
        Menu menu = menuRepository.findById(id)
                .orElseThrow(()->  new NotFoundException("해당하는 메뉴를 찾을 수 없습니다. id = "+id));

        menuRepository.delete(menu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
