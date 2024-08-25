package com.smart.tailor.event;

import com.smart.tailor.utils.response.OrderResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CreateOrderEvent extends ApplicationEvent {
    private OrderResponse orderResponse;

    public CreateOrderEvent(OrderResponse orderResponse) {
        super(orderResponse);
        this.orderResponse = orderResponse;
    }
}