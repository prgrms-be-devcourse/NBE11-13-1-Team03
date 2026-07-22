package com.team3.coffee_order.event;

import com.team3.coffee_order.service.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveredEventListener {

    private final SlackNotificationService slackNotificationService;

    // 주문 상태 변경 트랜잭션이 정상 커밋된 후 Slack 알림 전송
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DeliveredEvent event){
        try{
            slackNotificationService.sendDelivered(event);
        } catch (Exception e) {
            log.error(
                    "Slack 배송 완료 알림 전송 실패. orderId={}",
                    event.getOrderId(),
                    e
            );
        }
    }
}
