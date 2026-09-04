package com.mall.config;

import com.mall.common.redis.RedisChannelConstants;
import com.mall.product.cache.ProductCacheInvalidationSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * ClassName:RedisPubSubString
 * Package:com.mall.config
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 21:02
 * @Version: v1.0
 *
 */
@Configuration
public class RedisPubSubString {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            ProductCacheInvalidationSubscriber subscriber
    ){
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(
                subscriber,
                new ChannelTopic(RedisChannelConstants.PRODUCT_CACHE_INVALIDATE)
        );
        return container;
    }
}
