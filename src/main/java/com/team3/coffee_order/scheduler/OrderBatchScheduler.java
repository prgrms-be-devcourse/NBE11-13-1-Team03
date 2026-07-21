package com.team3.coffee_order.scheduler;

import com.team3.coffee_order.domain.entity.OrderStatus;
import com.team3.coffee_order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderBatchScheduler {
    private final OrderRepository orderRepository;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    //벌크 update를 사용
    @Transactional
    @Scheduled(cron="0 0 14 * * *",zone = "Asia/Seoul")
    public void updateOrderState(){
        log.info("[일일 주문 상태 변경 배치 작업 시작]");

        LocalDateTime endDateTime = LocalDate.now(KST).atTime(14, 0);
        LocalDateTime startDateTime = endDateTime.minusDays(1);

        List<Long> targetOrderIds = orderRepository.findIdsByCreatedAtBetweenAndStatus(
                startDateTime, endDateTime, OrderStatus.ORDERED);

        if (targetOrderIds.isEmpty()) {
            log.info("[일일 주문 상태 변경 배치 작업] 대상 주문 없음");
            return;
        }

        int updatedCount = orderRepository.bulkUpdateStatus(targetOrderIds, OrderStatus.SHIPPING);
        log.info("[일일 주문 상태 변경 배치 작업 완료] 대상 {}건, 변경 {}건", targetOrderIds.size(), updatedCount);
    }
}
