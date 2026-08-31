package com.mall.seckill.mq;

/**
 * ClassName:MqPublishResult
 * Package:com.mall.seckill.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 21:29
 * @Version: v1.0
 *
 */
public enum MqPublishResult {
    CONFIRMED,
    NACKED,
    RETURNED,
    UNKNOWN
}
