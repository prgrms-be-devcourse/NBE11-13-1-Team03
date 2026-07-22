package com.team3.coffee_order.service;

import com.team3.coffee_order.event.DeliveredEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Service
public class SlackNotificationService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final RestClient restClient;
    private final String webhookUrl;

    public SlackNotificationService(
            RestClient restClient,
            @Value("${slack.webhook-url}") String webhookUrl
    ) {
        this.restClient = restClient;
        this.webhookUrl = webhookUrl;
    }

    // 배송 완료 이벤트를 Slack 메세지로 변환하여 webhook으로 전송
    public void sendDelivered(DeliveredEvent event) {

        String message = createMessage(event);

        restClient.post()
                .uri(webhookUrl)
                .body(Map.of("text", message))
                .retrieve()
                .toBodilessEntity();
    }


    private String createMessage(DeliveredEvent event) {

        String deliverAt = event.getDeliveredAt().format(DATE_TIME_FORMATTER);
        String processingTime = formatDuration(event.getProcessingDuration());

        return """
                ✅ 배송 완료
                주문 번호: %d
                고객 이메일: %s
                배송지: %s
                우편번호: %s
                배송 완료 시각: %s
                주문 처리 소요 시간: %s
                """.formatted(
                        event.getOrderId(),
                event.getCustomerEmail(),
                event.getAddress(),
                event.getZipCode(),
                deliverAt,
                processingTime
        );
    }

    private String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();

        long day = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        return "%d일 %d시간 %d분".formatted(day, hours, minutes);
    }

}
