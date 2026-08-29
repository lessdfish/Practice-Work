package com.mall.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.aspect.OperationLog;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.common.redis.RedisLockService;
import com.mall.common.transaction.AfterCommitExecutor;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisLockService redisLockService;

    private static final long PRODUCT_CACHE_BASE_MINUTES = 30;
    private static final long PRODUCT_CACHE_RANDOM_MINUTES = 10;
    private static final String NULL_VALUE = "__NULL__";
    private static final Duration NULL_CACHE_TTL = Duration.ofMinutes(2);

    public ProductServiceImpl(ProductMapper productMapper, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper,RedisLockService redisLockService) {
        this.productMapper = productMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.redisLockService = redisLockService;
    }
    @Override
    @OperationLog("Finding specific")
    public Product getProductById(Long id){
        return loadProductWithMutex(id);
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

        AfterCommitExecutor.execute(()->stringRedisTemplate.delete(RedisKeyConstants.productDetail(productId)));
    }
}
