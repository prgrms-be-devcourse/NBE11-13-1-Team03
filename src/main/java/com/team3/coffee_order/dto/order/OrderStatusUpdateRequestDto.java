package com.team3.coffee_order.dto.order;

import com.team3.coffee_order.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {

    @NotNull(message = "주문 상태는 필수입니다.")
    private OrderStatus status;
}
