package com.mall.seckill.dto;

import lombok.Data;

/**
 * ClassName:SeckillSubmitResponse
 * Package:com.mall.seckill.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 20:31
 * @Version: v1.0
 *
 */
@Data
public class SeckillSubmitResponse {
    private String requestId;
    private String status;

    public SeckillSubmitResponse() {
    }

    public SeckillSubmitResponse(String requestId, String status) {
        this.requestId = requestId;
        this.status = status;
    }

}
