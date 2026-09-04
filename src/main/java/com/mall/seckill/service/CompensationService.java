package com.mall.seckill.service;

/**
 * ClassName:CompensationService
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:32
 * @Version: v1.0
 *
 */
public interface CompensationService {
    boolean compensate(Long activityId,Long userId);
}
