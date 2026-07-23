package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 발표용 동시성 비교 Service : 주문 생성 대신 재고 차감만 진행.
@Service
@RequiredArgsConstructor
public class DemoConcurrencyService {

    private final MenuRepository menuRepository;

    @Transactional
    public int decreaseStockWithoutLock(Long menuId, int quantity) {
        Menu menu = menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다. menuId=" + menuId));

        menu.decreaseStock(quantity);

        return menu.getStock();
    }
}
