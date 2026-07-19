package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.dto.MenuRequestDto;
import com.team3.coffee_order.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // TODO: create

    // TODO: read

    // TODO: update

    // TODO: delete
    public ResponseEntity<Void> deleteMenu(Long id){
        Menu menu = menuRepository.findById(id)
                .orElseThrow(()->  new NotFoundException("해당하는 메뉴를 찾을 수 없습니다. id = "+id));

        menuRepository.delete(menu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}