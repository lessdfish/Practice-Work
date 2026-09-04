package com.mall.product.cache;

import com.mall.common.redis.RedisChannelConstants;
import com.mall.common.redis.RedisKeyConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName:ProductCacheInvalidationService
 * Package:com.mall.product.cache
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 21:08
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCacheInvalidationService {

    private final StringRedisTemplate redisTemplate;
    private final ProductLocalCache productLocalCache;

    public void invalidate(Long productId){
        try {
            redisTemplate.delete(productCacheKey(productId));

            productLocalCache.invalidate(productId);

            redisTemplate.convertAndSend(
                    RedisChannelConstants.PRODUCT_CACHE_INVALIDATE,
                    productId.toString()
            );
        }catch (Exception e){
            productLocalCache.invalidate(productId);
            log.error("商品缓存失效广播失败，productId={}",productId,e);
        }
    }

    private String productCacheKey(Long productId){
        return RedisKeyConstants.productDetail(productId);
    }
}
