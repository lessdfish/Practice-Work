package com.mall.ranking.controller;

import com.mall.common.result.Result;
import com.mall.ranking.dto.ProductRankingResponse;
import com.mall.ranking.service.ProductRankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:ProductRankingController
 * Package:com.mall.ranking.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 21:36
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/rankings/products")
public class ProductRankingController {

    private final ProductRankingService productRankingService;

    public ProductRankingController(ProductRankingService productRankingService) {
        this.productRankingService = productRankingService;
    }

    @GetMapping
    public Result<List<ProductRankingResponse>> topProducts(@RequestParam(defaultValue = "10") int limit){
        int safeLimit = Math.min(Math.max(limit,1),100);
        return Result.success(productRankingService.top(safeLimit));
    }
}
