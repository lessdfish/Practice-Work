package com.mall.ranking.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.common.redis.RedisLockService;
import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
import com.mall.ranking.dto.ProductRankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ClassName:ProductRankingServiceImpl
 * Package:com.mall.ranking.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 21:13
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class ProductRankingServiceImpl implements ProductRankingService{
    private final StringRedisTemplate stringRedisTemplate;
    private final ProductService productService;
    private static final DefaultRedisScript<Long> PAID_RANKING_SCRIPT;

    static {
        PAID_RANKING_SCRIPT = new DefaultRedisScript<>();
        PAID_RANKING_SCRIPT.setLocation(
                new ClassPathResource(
                        "scripts/order_paid_ranking.lua"
                )
        );
        PAID_RANKING_SCRIPT.setResultType(Long.class);
    }
    @Override
    public void increasePaidQuantity(String eventId,Long productId, Integer quantity) {
        Long result = stringRedisTemplate.execute(PAID_RANKING_SCRIPT,
                List.of(RedisKeyConstants.PAID_RANKING,
                        RedisKeyConstants.paidRankingEvent(eventId)),
                String.valueOf(productId),
                String.valueOf(quantity),
                String.valueOf(7 * 24 * 60 * 60));

        if (result == null) {
            throw new IllegalStateException("更新支付销量排行榜失败");
        }
    }

    @Override
    public List<ProductRankingResponse> top(int limit) {
        if (limit<=0) {
            return List.of();
        }
        Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(
                RedisKeyConstants.PRODUCT_ORDER_RANKING,0,limit-1L
        );
        if (tuples == null || tuples.isEmpty()) {
            return List.of();
        }

        List<ProductRankingResponse> result = new ArrayList<>();
        int rank = 1;

        //TODO: Solve N+1 PROBLEM
        for(ZSetOperations.TypedTuple<String> tuple:tuples){
            String productIdString = tuple.getValue();

            Double score = tuple.getScore();

            if (productIdString == null || score == null) {
                continue;
            }
            Long productId = Long.valueOf(productIdString);

            Product product;
            try {
                product = productService.getProductById(productId);
            }catch (BusinessException e){
                continue;
            }

            ProductRankingResponse response = new ProductRankingResponse();

            response.setRank(rank);

            response.setProductId(productId);
            response.setProductName(product.getName());
            response.setPrice(product.getPrice());
            response.setOrderQuantity(score.longValue());

            result.add(response);
            rank++;
        }
        return result;
    }
}
