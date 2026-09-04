package com.mall.product.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mall.product.domain.Product;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

/**
 * ClassName:ProductLocalCache
 * Package:com.mall.product.cache
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 20:45
 * @Version: v1.0
 *
 */
@Component
public class ProductLocalCache {

    private final Cache<Long, Product> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofSeconds(30))
            .build();

    public Product get(Long productId, Function<Long,Product> loader){
        return cache.get(productId,loader);
    }

    public Product getIfPresent(Long productId){
        return cache.getIfPresent(productId);
    }
    public void put(Long productId,Product product){
        cache.put(productId,product);
    }
    public void invalidate(Long productId){
        cache.invalidate(productId);
    }

    public long size(){
        return cache.estimatedSize();
    }
}
