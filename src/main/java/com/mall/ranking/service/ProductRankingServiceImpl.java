package com.mall.ranking.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.common.redis.RedisLockService;
import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
import com.mall.ranking.dto.ProductRankingResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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
public class ProductRankingServiceImpl implements ProductRankingService{
    private final StringRedisTemplate stringRedisTemplate;
    private final ProductService productService;

    public ProductRankingServiceImpl(StringRedisTemplate stringRedisTemplate, ProductService productService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.productService = productService;
    }

    @Override
    public void increaseOrderQuantity(Long productId, Integer quantity) {
        stringRedisTemplate.opsForZSet().incrementScore(RedisKeyConstants.PRODUCT_ORDER_RANKING,
                String.valueOf(productId),quantity.doubleValue());
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
