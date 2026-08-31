package com.mall.config;

import com.mall.common.mq.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:RabbitMqConfig
 * Package:com.mall.config
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:21
 * @Version: v1.0
 *
 */
@Configuration
public class RabbitMqConfig {
    @Bean("seckillExchange")
    public DirectExchange seckillExchange() {
        return new DirectExchange(RabbitMqConstants.SECKILL_EXCHANGE, true, false);
    }

    @Bean("seckillDeadExchange")
    public DirectExchange seckillDeadExchange() {
        return new DirectExchange(RabbitMqConstants.SECKILL_DEAD_EXCHANGE, true, false);
    }

    @Bean("seckillOrderQueue")
    public Queue seckillOrderQueue() {
        return QueueBuilder
                .durable(RabbitMqConstants.SECKILL_ORDER_QUEUE)
                .deadLetterExchange(RabbitMqConstants.SECKILL_DEAD_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.SECKILL_ORDER_DEAD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue seckillOrderDeadQueue() {
        return QueueBuilder.durable(
                RabbitMqConstants.SECKILL_ORDER_DEAD_QUEUE
        ).build();
    }

    @Bean
    public Binding seckillOrderBinding(@Qualifier("seckillOrderQueue") Queue seckillOrderQueue, @Qualifier("seckillExchange") DirectExchange seckillExchange) {
        return BindingBuilder
                .bind(seckillOrderQueue)
                .to(seckillExchange)
                .with(RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding seckillOrderDeadBinding(@Qualifier("seckillOrderDeadQueue") Queue queue,
                                           @Qualifier("seckillDeadExchange") DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitMqConstants.SECKILL_ORDER_DEAD_ROUTING_KEY);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean("seckillRetryExchange")
    public DirectExchange seckillRetryExchange() {

        return new DirectExchange(
                RabbitMqConstants.SECKILL_RETRY_EXCHANGE,
                true,
                false
        );
    }


    @Bean("seckillRetry1Queue")
    public Queue seckillRetry1Queue() {

        return QueueBuilder
                .durable(RabbitMqConstants.SECKILL_RETRY_1_QUEUE)
                .ttl(1000)
                .deadLetterExchange(RabbitMqConstants.SECKILL_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY)
                .build();
    }


    @Bean("seckillRetry5Queue")
    public Queue seckillRetry5Queue() {

        return QueueBuilder
                .durable(RabbitMqConstants.SECKILL_RETRY_5_QUEUE)
                .ttl(5000)
                .deadLetterExchange(RabbitMqConstants.SECKILL_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY)
                .build();
    }


    @Bean("seckillRetry30Queue")
    public Queue seckillRetry30Queue() {

        return QueueBuilder
                .durable(RabbitMqConstants.SECKILL_RETRY_30_QUEUE)
                .ttl(30000)
                .deadLetterExchange(RabbitMqConstants.SECKILL_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding seckillRetry1Binding(
            @Qualifier("seckillRetry1Queue") Queue queue,
            @Qualifier("seckillRetryExchange") DirectExchange exchange
    ) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitMqConstants.SECKILL_RETRY_1_KEY);
    }


    @Bean
    public Binding seckillRetry5Binding(
            @Qualifier("seckillRetry5Queue") Queue queue,
            @Qualifier("seckillRetryExchange") DirectExchange exchange
    ) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitMqConstants.SECKILL_RETRY_5_KEY);
    }


    @Bean
    public Binding seckillRetry30Binding(@Qualifier("seckillRetry30Queue") Queue queue, @Qualifier("seckillRetryExchange") DirectExchange exchange
    ) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitMqConstants.SECKILL_RETRY_30_KEY);
    }
}
