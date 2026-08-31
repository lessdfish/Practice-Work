package com.mall.seckill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.order.domain.Order;
import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
import com.mall.seckill.domain.SeckillActivity;
import com.mall.seckill.mapper.SeckillActivityMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * ClassName:SeckillServiceImpl
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 00:16
 * @Version: v1.0
 *
 */
@Service
public class SeckillServiceImpl implements SeckillService{
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    public static final DefaultRedisScript<Long> COMPENSATE_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();

        SECKILL_SCRIPT.setLocation(new ClassPathResource("scripts/seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);

        COMPENSATE_SCRIPT = new DefaultRedisScript<>();
        COMPENSATE_SCRIPT.setLocation(new ClassPathResource("scripts/seckill_compensate.lua"));
        COMPENSATE_SCRIPT.setResultType(Long.class);
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillOrderService seckillOrderService;
    private final ProductService productService;

    public SeckillServiceImpl(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper, SeckillActivityMapper seckillActivityMapper, SeckillOrderService seckillOrderService, ProductService productService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.seckillActivityMapper = seckillActivityMapper;
        this.seckillOrderService = seckillOrderService;
        this.productService = productService;
    }

    @Override
    public void preheat(Long activityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);

        if (activity == null) {
            throw new BusinessException(ErrorCode.SECKILL_ACTIVITY_NOT_FOUND);
        }
        if (activity.getStatus() != 1) {
            throw new BusinessException(ErrorCode.SECKILL_DISABLED);
        }

        LocalDateTime now = LocalDateTime.now();

        if (!now.isBefore(activity.getStartTime())) {
            throw new BusinessException("Only Pre",ErrorCode.PARAM_ERROR);
        }

        Product product = productService.getProductById(activity.getProductId());
        if (product.getStatus()!=1) {
            throw new BusinessException(ErrorCode.PRODUCT_OFF_SHELF);
        }

        String activityKey = RedisKeyConstants.seckillActivity(activityId);
        String stockKey = RedisKeyConstants.seckillStock(activityId);
        String userKey = RedisKeyConstants.seckillUsers(activityId);

        LocalDateTime expireTime = activity.getEndTime().plusDays(1);

        long ttlSeconds = Duration.between(now,expireTime).getSeconds();

        if (ttlSeconds<=0) {
            throw new BusinessException(ErrorCode.SECKILL__ENDED);
        }

        try {
            String json = objectMapper.writeValueAsString(activity);
            stringRedisTemplate.opsForValue().set(activityKey,json,Duration.ofSeconds(ttlSeconds));

            stringRedisTemplate.opsForValue().set(stockKey,String.valueOf(activity.getAvailableStock()),Duration.ofSeconds(ttlSeconds));
            stringRedisTemplate.delete(userKey);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Seckill Redis Preheat Failed!",e);
        }

    }

    @Override
    public Order seckill(Long activityId, Long userId) {
        String activityKey = RedisKeyConstants.seckillActivity(activityId);
        String json = stringRedisTemplate.opsForValue().get(activityKey);

        if (json == null) {
            throw new BusinessException(ErrorCode.SECKILL_ACTIVITY_NOT_PREHEATED);
        }

        SeckillActivity activity;

        try {
            activity = objectMapper.readValue(json,SeckillActivity.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("SeckillActivity ReverseSerial Failed",e);
        }

        if (activity.getStatus()!=1) {
            throw new BusinessException(ErrorCode.SECKILL_DISABLED);
        }

        String stockKey = RedisKeyConstants.seckillStock(activityId);
        String userKey = RedisKeyConstants.seckillUsers(activityId);

        long startEpoch = activity.getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond();
        long endEpoch = activity.getEndTime().atZone(ZoneId.systemDefault()).toEpochSecond();

        long expireAtEpoch = activity.getEndTime().plusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond();
        Long result = stringRedisTemplate.execute(SECKILL_SCRIPT,List.of(stockKey,userKey),String.valueOf(userId),String.valueOf(startEpoch),String.valueOf(endEpoch),String.valueOf(expireAtEpoch));

        if (result == null) {
            throw new RuntimeException("Run Lua Failed");
        }

        switch (result.intValue()){
            case 1 -> throw new BusinessException(ErrorCode.SECKILL_SOLD_OUT);
            case 2 -> throw new BusinessException(ErrorCode.SECKILL_DUPLICATE_ORDER);
            case 3 -> throw new BusinessException(ErrorCode.SECKILL_ACTIVITY_NOT_PREHEATED);
            case 4 -> throw new BusinessException(ErrorCode.SECKILL_ACTIVITY_NOT_START);
            case 5 -> throw new BusinessException(ErrorCode.SECKILL__ENDED);
            case 0 ->{/* Success */ }
            default -> throw new RuntimeException("Not Seckill Lua value: "+ result);
        }

        try {
            return seckillOrderService.createOrder(userId,activity);
        }catch (BusinessException e){
            if (e.getErrorCode() == ErrorCode.SECKILL_DUPLICATE_ORDER) {
                throw e;
            }

            compensate(stockKey,userKey,userId);
            throw e;
        }catch (Exception e){
            compensate(stockKey,userKey,userId);
            throw e;
        }
    }

    private void compensate(String stockKey,String userKey,Long userId){
        try {
            stringRedisTemplate.execute(COMPENSATE_SCRIPT,List.of(stockKey,userKey),String.valueOf(userId));
        }catch (Exception e){
            System.err.println("SecKill Redis Compensate Failed, userId = " + userId);
            e.printStackTrace();
        }
    }
}
