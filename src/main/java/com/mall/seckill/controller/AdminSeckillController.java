package com.mall.seckill.controller;

import com.mall.common.result.Result;
import com.mall.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:AdminSeckillController
 * Package:com.mall.seckill.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 19:12
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/admin/seckill/activities")
public class AdminSeckillController {
    private final SeckillService seckillService;

    public AdminSeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @PostMapping("/{activityId}/preheat")
    public Result<Void> preheat(@PathVariable Long activityId){
        seckillService.preheat(activityId);
        return Result.success();
    }
}
