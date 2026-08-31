package com.mall.common.redis;

/**
 * ClassName:RedisKeyConstants
 * Package:com.mall.common.redis
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 15:43
 * @Version: v1.0
 *
 */
public final class RedisKeyConstants {
    public RedisKeyConstants() {
    }

    public static final String PRODUCT_DETAIL = "mall:product:detail:";
    public static final String PRODUCT_DETAIL_LOCK = "mall:lock:product:detail:";
    public static final String CART = "mall:cart:";
    public static final String PRODUCT_ORDER_RANKING = "mall:ranking:product:order";
    public static final String SECKILL_ACTIVITY = "mall:seckill:activity:";
    public static final String SECKILL_STOCK = "mall:seckill:stock:";
    public static final String SECKILL_USERS = "mall:seckill:users:";
    public static final String SECKILL_RESULT = "mall:seckill:result:";
    public static final String SECKILL_OUTBOX_STREAM = "mall:seckill:outbox";

    public static String productDetail(Long productId){
        return PRODUCT_DETAIL + productId;
    }

    public static String productDetailLock(Long productId){
        return PRODUCT_DETAIL_LOCK + productId;
    }
    public static String cart(Long userId){
        return CART + userId;
    }

    public static String seckillActivity(Long activityId){
        return SECKILL_ACTIVITY + activityId;
    }
    public static String seckillStock(Long activityId) {
        return SECKILL_STOCK + activityId;
    }
    public static String seckillUsers(Long activityId) {
        return SECKILL_USERS + activityId;
    }
    public static String seckillResult(String requestId){ return SECKILL_RESULT + requestId; }
}
