package com.team3.coffee_order.mapper;

import com.team3.coffee_order.domain.entity.Order;
import com.team3.coffee_order.event.DeliveredEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class DeliveredEventMapper {
    // 배송 완료 주문 정보를 Slack 알림 전송용 이벤트 객체로 변환
    public DeliveredEvent toDeliveredEvent(Order order){
        LocalDateTime deliveredAt = LocalDateTime.now();
        
        Duration processingDuration = 
                Duration.between(order.getCreatedAt(), deliveredAt);
        
        return new DeliveredEvent(
                order.getId(),
                order.getCustomer().getEmail(),
                order.getAddress(),
                order.getZipCode(),
                deliveredAt,
                processingDuration
        );
    }

}
