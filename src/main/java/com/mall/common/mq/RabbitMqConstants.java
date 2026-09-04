package com.mall.common.mq;

/**
 * ClassName:RabbitMqConstants
 * Package:com.mall.common.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:18
 * @Version: v1.0
 *
 */
public class RabbitMqConstants {
    private RabbitMqConstants() {
    }

    public static final String SECKILL_EXCHANGE = "mall.seckill.exchange";
    public static final String SECKILL_ORDER_QUEUE = "mall.seckill.order.queue";
    public static final String SECKILL_ORDER_ROUTING_KEY = "mall.seckill.order.create";
    public static final String SECKILL_DEAD_EXCHANGE = "mall.seckill.dead.exchange";
    public static final String SECKILL_ORDER_DEAD_QUEUE = "mall.seckill.order.dead.queue";
    public static final String SECKILL_ORDER_DEAD_ROUTING_KEY = "mall.seckill.order.dead";
    public static final String SECKILL_RETRY_EXCHANGE = "mall.seckill.retry.exchange";
    public static final String SECKILL_RETRY_1_QUEUE = "mall.seckill.order.retry.1s.queue";
    public static final String SECKILL_RETRY_5_QUEUE = "mall.seckill.order.retry.5s.queue";
    public static final String SECKILL_RETRY_30_QUEUE = "mall.seckill.order.retry.30s.queue";
    public static final String SECKILL_RETRY_1_KEY = "mall.seckill.order.retry.1s";
    public static final String SECKILL_RETRY_5_KEY = "mall.seckill.order.retry.5s";
    public static final String SECKILL_RETRY_30_KEY = "mall.seckill.order.retry.30s";
    public static final String ORDER_EXCHANGE = "mall.order.exchange";
    public static final String ORDER_TIMEOUT_DELAY_QUEUE = "mall.order.timeout.delay.queue";
    public static final String ORDER_TIMEOUT_QUEUE = "mall.order.timeout.queue";
    public static final String ORDER_TIMEOUT_DELAY_KEY = "mall.order.timeout.delay";
    public static final String ORDER_TIMEOUT_KEY = "mall.order.timeout.close";
    public static final String ORDER_PAID_QUEUE = "mall.order.paid.queue";
    public static final String ORDER_PAID_KEY = "mall.order.paid";

}
