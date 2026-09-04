package com.mall.seckill.mq;

import com.mall.common.mq.RabbitMqConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ClassName:SeckillOrderProducer
 * Package:com.mall.seckill.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:28
 * @Version: v1.0
 *
 */
@Component
public class SeckillOrderProducer {
    public static final Logger log = LoggerFactory.getLogger(SeckillOrderProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public SeckillOrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public MqPublishResult send(SeckillOrderMessage message){
        return publish(RabbitMqConstants.SECKILL_EXCHANGE,RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY,message);
    }
    public MqPublishResult sendRetry(SeckillOrderMessage message){
        int retryCount = message.getRetryCount();
        String routingKey;

        if (retryCount == 1) {
            routingKey = RabbitMqConstants.SECKILL_RETRY_1_KEY;
        } else if (retryCount == 2) {
            routingKey = RabbitMqConstants.SECKILL_RETRY_5_KEY;
        }else {
            routingKey = RabbitMqConstants.SECKILL_RETRY_30_KEY;
        }

        return publish(RabbitMqConstants.SECKILL_RETRY_EXCHANGE,routingKey,message);
    }

    private MqPublishResult publish(String exchange,String routingKey,SeckillOrderMessage message){
        CorrelationData correlationData = new CorrelationData(message.getRequestId());

        try {
            rabbitTemplate.convertAndSend(RabbitMqConstants.SECKILL_EXCHANGE,
                    RabbitMqConstants.SECKILL_ORDER_ROUTING_KEY,message,
                    amqpMessage ->{
                        amqpMessage.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        amqpMessage.getMessageProperties().setMessageId(message.getRequestId());
                        return amqpMessage;
                    },
                    correlationData);

            CorrelationData.Confirm confirm = correlationData.getFuture().get(3, TimeUnit.SECONDS);

            if (correlationData.getReturned()!=null){
                log.error("RabbitMQ Message can't Routing, requestId={}, replyText={}, exchange={}, routingKey={}"
                        ,message.getRequestId(),correlationData.getReturned().getReplyText(),correlationData.getReturned().getReplyText(),correlationData.getReturned().getRoutingKey());
                return MqPublishResult.RETURNED;
            }
            if (!confirm.isAck()) {
                log.error("RabbitMQ Publisher NACK,requestId={}, reason={}",
                        message.getRequestId(),confirm.getReason());
                return MqPublishResult.NACKED;
            }
            log.info("RabbitMQ Publisher Confirm Success,requestId={}",message.getRequestId());
            return MqPublishResult.CONFIRMED;
        }catch (TimeoutException e){
            log.error("Waiting RabbitMQ Confirm Timeout,requestId={}",message.getRequestId(),e);
            return MqPublishResult.UNKNOWN;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("Waiting Confirm BE interrupt,requestId={}",message.getRequestId(),e);
            return MqPublishResult.UNKNOWN;
        }catch (ExecutionException e){
            log.error("Confirm Error requestId={}",message.getRequestId(),e);
            return MqPublishResult.UNKNOWN;
        }catch (Exception e){
            log.error("Send Error,requestId={}",message.getRequestId(),e);
            return MqPublishResult.UNKNOWN;
        }
    }


}
