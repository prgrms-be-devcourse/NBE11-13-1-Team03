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

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    // TODO: create

    // TODO: read
    @Transactional(readOnly = true)
    public List<MenuGetResponse> getMenus() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(menuMapper::toMenuGetResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuGetResponse getMenu(long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        return menuMapper.toMenuGetResponse(menu);
    }

    // TODO: update

    // TODO: delete
}