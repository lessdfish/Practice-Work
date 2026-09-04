package com.mall.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.aspect.OperationLog;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.common.redis.RedisLockService;
import com.mall.common.transaction.AfterCommitExecutor;
import com.mall.product.cache.ProductCacheInvalidationService;
import com.mall.product.cache.ProductLocalCache;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName:ProductServiceImpl
 * Package:com.mall.product.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:48
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisLockService redisLockService;
    private final ProductLocalCache productLocalCache;
    private final ProductCacheInvalidationService cacheInvalidationService;

    private static final long PRODUCT_CACHE_BASE_MINUTES = 30;
    private static final long PRODUCT_CACHE_RANDOM_MINUTES = 10;
    private static final String NULL_VALUE = "__NULL__";
    private static final Duration NULL_CACHE_TTL = Duration.ofMinutes(2);


    @Override
    public Map<Long, Product> getProductByIds(Collection<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }

        List<Long> ids = productIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long,Product> result = new HashMap<>(ids.size());
        List<Long> redisIds = new ArrayList<>();

        // L1: Caffine
        for (Long id : ids) {
            Product product = productLocalCache.getIfPresent(id);

            if (product!=null) {
                result.put(id,product);
            }else {
                redisIds.add(id);
            }
        }

        if (redisIds.isEmpty()) {
            return result;
        }

        // L2: Redis

        List<String> keys = redisIds.stream()
                .map(RedisKeyConstants::productDetail)
                .toList();

        List<String> cachedValues = stringRedisTemplate.opsForValue().multiGet(keys);

        List<Long> dbIds = new ArrayList<>();

        for (int i = 0; i < redisIds.size(); i++) {
            Long id = redisIds.get(i);

            String cached = cachedValues == null ? null : cachedValues.get(i);

            if (cached == null){
                dbIds.add(id);
                continue;
            }

            if (NULL_VALUE.equals(cached)){
                continue;
            }

            Product product = readProduct(cached);

            result.put(id,product);
            productLocalCache.put(id,product);
        }
        if (dbIds.isEmpty()){
            return result;
        }

        // L3 :MYSQL

        List<Product> dbProducts = productMapper.selectBatchIds(dbIds);
        Map<Long,Product> dbProductMap = dbProducts.stream().collect(
                Collectors.toMap(
                        Product::getId,
                        Function.identity()
                )
        );
        for (Long id : dbIds) {
            Product product = dbProductMap.get(id);
            String cacheKey = RedisKeyConstants.productDetail(id);

            if (product == null) {
                stringRedisTemplate.opsForValue().set(
                        cacheKey,
                        NULL_VALUE,
                        NULL_CACHE_TTL
                );
                continue;
            }
            writeProductCache(cacheKey,product);
            productLocalCache.put(id,product);
            result.put(id,product);
        }
        return result;
    }

    @Override
    @OperationLog("Finding specific")
    public Product getProductById(Long productId){
        return productLocalCache.get(
                productId,
                this::loadProductWithMutex
        );
    }

    private Product loadProductWithMutex(Long id){
        String cacheKey = RedisKeyConstants.productDetail(id);
        String lockKey = RedisKeyConstants.productDetailLock(id);
        while (true){
            String cached = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cached!=null) {
                if (NULL_VALUE.equals(cached)) {
                    throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
                }
                return readProduct(cached);
            }
            String lockValue = redisLockService.tryLock(lockKey,Duration.ofSeconds(10));

            if (lockValue == null) {
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    Thread.currentThread()
                            .interrupt();
                    throw new RuntimeException("Waiting Product Be Interrupt",e);
                }
                continue;
            }

            try {
                cached = stringRedisTemplate.opsForValue().get(cacheKey);
                if (cached!=null) {
                    if (NULL_VALUE.equals(cached)){
                        throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
                    }
                    return readProduct(cached);
                }
                Product product = productMapper.selectById(id);

                if (product == null) {
                    stringRedisTemplate.opsForValue().set(cacheKey,NULL_VALUE,NULL_CACHE_TTL);
                    throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
                }
                writeProductCache(cacheKey,product);
                return product;
            }finally {
                redisLockService.unlock(lockKey,lockValue);
            }
        }
    }

    @Override
    public List<Product> listProducts() {
        return productMapper.selectList(null);
    }

    private void writeProductCache(String key,Product product){
        try {
            String json = objectMapper.writeValueAsString(product);

            long randomMinutes = ThreadLocalRandom.current().nextLong(PRODUCT_CACHE_RANDOM_MINUTES+1);
            Duration ttl = Duration.ofMinutes(PRODUCT_CACHE_BASE_MINUTES+randomMinutes);

            stringRedisTemplate.opsForValue().set(key,json,ttl);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Json Serial Fail",e);
        }
    }

    private Product readProduct(String json){
        try {
            return objectMapper.readValue(json,Product.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("商品缓存反序列化失败",e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStatus(Long productId, Integer status) {
        Product product = productMapper.selectById(productId);

        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        product.setStatus(status);

        int updated = productMapper.updateById(product);

        if (updated!=1) {
            throw new BusinessException("Product Information Update Failed",ErrorCode.SYSTEM_ERROR);
        }

        AfterCommitExecutor.execute(()->cacheInvalidationService.invalidate(productId));
    }
}
