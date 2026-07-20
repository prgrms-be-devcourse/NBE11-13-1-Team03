package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // TODO: create
    public Menu getMenuEntityById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    // TODO: read

    // TODO: update

    // TODO: delete
}