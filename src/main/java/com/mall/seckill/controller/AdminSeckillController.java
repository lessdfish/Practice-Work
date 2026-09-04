package com.mall.seckill.controller;

import com.mall.common.result.Result;
import com.mall.seckill.service.ReconcileService;
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
    private final ReconcileService reconcileService;

    public AdminSeckillController(SeckillService seckillService,ReconcileService reconcileService) {
        this.seckillService = seckillService;
        this.reconcileService = reconcileService;
    }

    @PostMapping("/{activityId}/preheat")
    public Result<Void> preheat(@PathVariable Long activityId){
        seckillService.preheat(activityId);
        return Result.success();
    }

    @PostMapping("/{activityId}/reconcile/{requestId}")
    public Result<Void> reconcile(
            @PathVariable Long activityId,
            @PathVariable String requestId){
        reconcileService.reconcile(activityId,requestId);
        return Result.success();
    }


}
