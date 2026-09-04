package com.mall.seckill.domain;

/**
 * ClassName:SeckillRequestRecord
 * Package:com.mall.seckill.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:37
 * @Version: v1.0
 *
 */
public record SeckillRequestRecord (String requestId,Long activityId,Long userId,String status,Long orderId){

}
