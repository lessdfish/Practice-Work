package com.mall.seckill.dto;

import lombok.Data;

/**
 * ClassName:SeckillResultResponse
 * Package:com.mall.seckill.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 22:46
 * @Version: v1.0
 *
 */
@Data
public class SeckillResultResponse {
    private String requestId;
    private String status;
    private Long orderId;
    private String message;
}
