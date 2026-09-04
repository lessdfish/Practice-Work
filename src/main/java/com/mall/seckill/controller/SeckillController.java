package com.mall.seckill.controller;

import com.mall.common.result.Result;
import com.mall.order.domain.Order;
import com.mall.seckill.dto.SeckillResultResponse;
import com.mall.seckill.dto.SeckillSubmitResponse;
import com.mall.seckill.service.SeckillResultService;
import com.mall.seckill.service.SeckillService;
import com.mall.security.MallUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    private final SeckillResultService seckillResultService;

    public SeckillController(SeckillService seckillService, SeckillResultService seckillResultService) {
        this.seckillService = seckillService;
        this.seckillResultService = seckillResultService;
    }

    @PostMapping("/{activityId}")
    public Result<SeckillSubmitResponse> seckill(@PathVariable Long activityId,
                                                 @AuthenticationPrincipal MallUserDetails currentUser){
        return Result.success(seckillService.seckill(activityId,currentUser.getUserId()));
    }

    @GetMapping("/{activityId}/results/{requestId}")
    public Result<SeckillResultResponse> getResult(
            @PathVariable Long activityId,
            @PathVariable String requestId,
            @AuthenticationPrincipal MallUserDetails currentUser){
        return Result.success(seckillResultService.getResult(activityId,requestId,currentUser.getUserId()));
    }
}
