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
        // 변경 전: return menuRepository.findById(menuId)
        return menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));
    }

    //주문 생성 시, 락을 걸고 재고 차감(비관적 락 적용)
    @Transactional
    public Menu decreaseStockForOrder(Long menuId, int quantity) {
        Menu menu = menuRepository.findByIdForUpdate(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        menu.decreaseStock(quantity);

        return menu;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        // 변경 전: if (menuRepository.existsByName(request.getName()))
        if (menuRepository.existsByNameAndDeletedFalse(request.getName()))
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
        // 변경 전: return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
        return menuRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(menuMapper::toMenuGetResponse)
                .toList();
    }

    public MenuGetResponse getMenu(Long menuId) {
        // 변경 전: Menu menu = menuRepository.findById(menuId)
        Menu menu = menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        return menuMapper.toMenuGetResponse(menu);
    }

    // TODO: update
    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest request) {
        // 검증된 요청 값으로 메뉴를 조회하고, 엔티티 변경 메서드로 수정 결과를 반환한다.
        // 변경 전: Menu menu = menuRepository.findById(menuId)
        Menu menu = menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));

        menu.update(request.getName(), request.getPrice(), request.getStock(), request.getDescription());

        return new MenuResponse(menu);
    }

    // TODO: delete
    @Transactional
    public void deleteMenu(Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->  new NotFoundException("메뉴를 찾을 수 없습니다. menuId="+menuId));

        menuRepository.delete(menu);
    }
}
