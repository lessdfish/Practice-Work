package com.mall.seckill.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:SeckillOrderMessage
 * Package:com.mall.seckill.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:26
 * @Version: v1.0
 *
 */
@Data
public class SeckillOrderMessage implements Serializable {
    private String requestId;
    private Long activityId;
    private Long userId;
    private Integer retryCount = 0;

    public SeckillOrderMessage() {
    }

    public SeckillOrderMessage(String requestId, Long activityId, Long userId) {
        this.requestId = requestId;
        this.activityId = activityId;
        this.userId = userId;
        this.retryCount = 0;
    }
}
