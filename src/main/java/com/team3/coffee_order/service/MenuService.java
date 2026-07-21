package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.dto.menu.MenuCreateRequest;
import com.team3.coffee_order.dto.menu.MenuGetResponse;
import com.team3.coffee_order.dto.menu.MenuResponse;
import com.team3.coffee_order.dto.menu.MenuUpdateRequest;
import com.team3.coffee_order.exception.DuplicateMenuNameException;
import com.team3.coffee_order.exception.NotFoundException;
import com.team3.coffee_order.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    // TODO: create
    public Menu getMenuEntity(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        if (menuRepository.existsByName(request.getName()))
            throw new DuplicateMenuNameException("이미 존재하는 메뉴입니다.");

        String description = (request.getDescription() == null || request.getDescription().isBlank())
                ? null
                : request.getDescription();
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getStock(), description);

        return new MenuResponse(menuRepository.save(menu));
    }

    // TODO: read
    // 응답 순서를 고정하기 위해 id 오름차순으로 조회한다.
    public List<MenuGetResponse> getMenus() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(menuMapper::toMenuGetResponse)
                .toList();
    }

    public MenuGetResponse getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        return menuMapper.toMenuGetResponse(menu);
    }

    // TODO: update
    @Transactional
    public ResponseEntity<MenuResponse> updateMenu(Long menuId, MenuUpdateRequest request) {
        // 검증된 요청 값으로 메뉴를 조회하고, 엔티티 변경 메서드로 수정 결과를 반환한다.
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));

        menu.update(request.getName(), request.getPrice(), request.getStock(), request.getDescription());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MenuResponse(menu));
    }

    // TODO: delete
    @Transactional
    public ResponseEntity<Void> deleteMenu(Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->  new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));

        menuRepository.delete(menu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
