package com.mall.seckill.mq;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.mq.RabbitMqConstants;
import com.mall.seckill.service.SeckillOrderService;
import com.mall.seckill.service.SeckillResultService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.rabbitmq.client.Channel;

/**
 * ClassName:SeckillOrderConsumer
 * Package:com.mall.seckill.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:50
 * @Version: v1.0
 *
 */
@Component
@RequiredArgsConstructor
public class SeckillOrderConsumer {
    public static final Logger log = LoggerFactory.getLogger(SeckillOrderConsumer.class);
    private final SeckillOrderService seckillOrderService;
    private final SeckillResultService seckillResultService;
    private final SeckillOrderProducer seckillOrderProducer;

    @RabbitListener(queues = RabbitMqConstants.SECKILL_ORDER_QUEUE)
    public void consume(SeckillOrderMessage message, Message amqpMessage, Channel channel) throws IOException {
        long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();
        seckillResultService.markProcessing(message.getActivityId(), message.getRequestId());
        try {
            Long orderId = seckillOrderService.createOrder(message.getUserId(), message.getActivityId());
            try {
                seckillResultService.markSuccess(message.getActivityId(), message.getRequestId(), orderId);
            }catch (Exception e){
                log.error("Already Success,Update Failed,requestId={}",message.getRequestId(),e);
            }
            channel.basicAck(deliveryTag,false);

        }catch (BusinessException e){
            if (e.getErrorCode() == ErrorCode.SECKILL_DUPLICATE_ORDER) {
                Long orderId = seckillOrderService.findExistingOrderId(message.getUserId(), message.getActivityId());
                try {
                    seckillResultService.markSuccess(message.getActivityId(), message.getRequestId(), orderId);
                }catch (Exception statusException){
                    log.error("幂等订单已存在，更新状态失败，requestId={}",message.getRequestId(),statusException);

                }
                channel.basicAck(deliveryTag,false);
                return;
            }
            seckillResultService.markFailed(message.getActivityId(), message.getRequestId(), e.getMessage());
            channel.basicNack(deliveryTag,false,false);
        }catch (Exception e){
            retryOrDLQ(message,deliveryTag,channel,e);
        }
    }

    private void retryOrDLQ(SeckillOrderMessage message,long deliveryTag,Channel channel,Exception exception)throws IOException{
        int currentRetry = message.getRetryCount() == null?0:message.getRetryCount();

        if (currentRetry<3) {
            message.setRetryCount(currentRetry+1);

            MqPublishResult result = seckillOrderProducer.sendRetry(message);

            if (result == MqPublishResult.CONFIRMED) {
                channel.basicAck(deliveryTag,false);
                log.warn("SecKill Order in {} count retry,requestId={}",message.getRetryCount(),message.getRequestId());
                return;
            }
            channel.basicNack(deliveryTag,false,true);
            return;
        }
        seckillResultService.markFailed(message.getActivityId(),message.getRequestId(), "Multi Retry Failed,Waiting System recovery");
        channel.basicNack(deliveryTag,false,false);
        log.error("More than Max retry, Send in DLQ,requestId={}",message.getRequestId(),exception);
    }
}
