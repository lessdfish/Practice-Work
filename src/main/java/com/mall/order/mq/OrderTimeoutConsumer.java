package com.mall.order.mq;

import com.mall.common.mq.RabbitMqConstants;
import com.mall.order.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName:OrderTimeoutConsumer
 * Package:com.mall.order.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:31
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutConsumer {
    private final OrderTimeoutService orderTimeoutService;

    @RabbitListener(
            queues = RabbitMqConstants.ORDER_TIMEOUT_QUEUE,
            ackMode = "AUTO"
    )
    public void consume(OrderTimeoutMessage message){
        boolean closed = orderTimeoutService.closeTimeoutOrder(message.orderId());
        log.info("订单延迟关闭处理完成，orderId={},closed={}",
                message.orderId(),closed);
    }
}
