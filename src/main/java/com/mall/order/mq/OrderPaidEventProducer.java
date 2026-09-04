package com.mall.order.mq;

import com.mall.common.mq.RabbitMqConstants;
import com.mall.order.event.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName:OrderPaidEventProducer
 * Package:com.mall.order.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:16
 * @Version: v1.0
 *
 */
@Component
@RequiredArgsConstructor
public class OrderPaidEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void send(OrderPaidEvent event){
        rabbitTemplate.convertAndSend(
                RabbitMqConstants.ORDER_EXCHANGE,
                RabbitMqConstants.ORDER_PAID_KEY,
                event
        );
    }
}
