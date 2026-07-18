package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.repository.MenuRepository;
import com.team3.coffee_order.dto.menu.MenuResponseDto;
import com.team3.coffee_order.dto.menu.MenuUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // TODO: create

    // TODO: read

    // TODO: update
    @Transactional
    public ResponseEntity<MenuResponseDto> updateMenu(Long menuId, MenuUpdateRequestDto request) {
        validateUpdateRequest(request);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found."));

        menu.update(request.getName(), request.getPrice(), request.getDescription());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MenuResponseDto(menu));
    }

    // TODO: delete

    private void validateUpdateRequest(MenuUpdateRequestDto request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required.");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu name is required.");
        }
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu price must be greater than or equal to 0.");
        }
    }
}
