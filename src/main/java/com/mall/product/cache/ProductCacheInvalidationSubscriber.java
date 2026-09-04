package com.mall.product.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * ClassName:ProductCacheInvalidationSubscriber
 * Package:com.mall.product.cache
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 20:54
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCacheInvalidationSubscriber implements MessageListener {
    private final ProductLocalCache productLocalCache;

    @Override
    public void onMessage(Message message,byte[] pattern) {
        String value = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            Long productId = Long.valueOf(value);
            productLocalCache.invalidate(productId);

            log.info("本地商品缓存已失效，productId={}",productId);
        }catch (NumberFormatException e){
            log.warn("收到非法商品缓存失效消息：{}",value);
        }
    }
}
