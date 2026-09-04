package com.mall.order.mq;

import com.mall.common.mq.RabbitMqConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName:OrderTimeoutProducer
 * Package:com.mall.order.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:25
 * @Version: v1.0
 *
 */
@Component
@RequiredArgsConstructor
public class OrderTimeoutProducer {
    private final RabbitTemplate rabbitTemplate;

    public void send(Long orderId){
        rabbitTemplate.convertAndSend(
                RabbitMqConstants.ORDER_EXCHANGE,
                RabbitMqConstants.ORDER_TIMEOUT_DELAY_KEY,
                new OrderTimeoutMessage(orderId)
        );
    }
}
