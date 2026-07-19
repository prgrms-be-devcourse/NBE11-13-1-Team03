package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.domain.repository.OrderItemRepository;
import com.team3.coffee_order.domain.repository.OrderRepository;
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

    // TODO: delete
    public ResponseEntity<Void> deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("해당하는 주문을 찾을 수 없습니다. id = "+id));

        orderRepository.delete(order);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}