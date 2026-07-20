package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
import com.team3.coffee_order.dto.order.OrderStatusResponseDto;
import com.team3.coffee_order.dto.order.OrderStatusUpdateRequestDto;
import com.team3.coffee_order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team3.coffee_order.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    // TODO: create

    // TODO: read

    // TODO: update
    @Transactional
    public ResponseEntity<OrderStatusResponseDto> updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        // 검증된 요청 값으로 주문을 조회하고, 주문 상태만 변경해 결과를 반환한다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        order.updateStatus(request.getStatus());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OrderStatusResponseDto(order));
    }

    // TODO: delete
}
    public ResponseEntity<Void> deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("해당하는 주문을 찾을 수 없습니다. id = "+id));

        orderRepository.delete(order);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
