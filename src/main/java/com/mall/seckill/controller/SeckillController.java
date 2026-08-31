package com.mall.seckill.controller;

import com.mall.common.result.Result;
import com.mall.order.domain.Order;
import com.mall.seckill.service.SeckillService;
import com.mall.security.MallUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:SeckillController
 * Package:com.mall.seckill.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 19:10
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/seckill")
public class SeckillController {
    private final SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @PostMapping("/{activityId}")
    public Result<Order> seckill(@PathVariable Long activityId,
                                 @AuthenticationPrincipal MallUserDetails currentUser){
        Order order = seckillService.seckill(activityId,currentUser.getUserId());
        return Result.success(order);
    }
}
