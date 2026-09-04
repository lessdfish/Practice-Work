package com.mall.order.mq;

import com.mall.common.mq.RabbitMqConstants;
import com.mall.order.event.OrderPaidEvent;
import com.mall.ranking.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName:OrderPaidRankingConsumer
 * Package:com.mall.order.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:23
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidRankingConsumer {
    private final ProductRankingService productRankingService;

    @RabbitListener(queues = RabbitMqConstants.ORDER_PAID_QUEUE)
    public void consume(OrderPaidEvent event){
        productRankingService.increasePaidQuantity(
                event.eventId(),
                event.productId(),
                event.quantity()
        );

        log.info("支付销量榜更新完成，eventId={},orderId={},productId={},quantity={}",
                event.eventId(),
                event.orderId(),
                event.productId(),
                event.quantity());
    }
}
