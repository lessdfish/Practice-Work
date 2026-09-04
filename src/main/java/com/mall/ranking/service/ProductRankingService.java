package com.mall.ranking.service;

import com.mall.ranking.dto.ProductRankingResponse;

import java.util.List;

/**
 * ClassName:ProductRankingService
 * Package:com.mall.ranking.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 21:12
 * @Version: v1.0
 *
 */
public interface ProductRankingService {
    void increasePaidQuantity(String eventId, Long productId,Integer quantity);

    List<ProductRankingResponse> top(int limit);
}
