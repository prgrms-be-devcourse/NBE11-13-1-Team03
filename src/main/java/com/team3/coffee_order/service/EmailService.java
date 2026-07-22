package com.team3.coffee_order.service;


import com.team3.coffee_order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOrderConfirmation(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getCustomer().getEmail());
            message.setSubject("[얼죽아연] 주문이 접수되었습니다.");
            message.setText(buildEmailBody(order));
            mailSender.send(message);
            log.info("주문 완료 메일 발신 성공: orderId={}", order.getId());
        } catch (MailException e) {
            log.error("주문 완료 메일 발신 실패: orderId={}", order.getId(), e);
        }
    }

    // 현재는 간단한 주문 정보만 이메일에 담아 발송
    private String buildEmailBody(Order order) {
        return "주문이 접수되었습니다.\n\n"
                + "주문번호: " + order.getId() + "\n"
                + "배송주소: " + order.getAddress();
    }
}
