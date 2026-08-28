package com.mall.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName:MallProperties
 * Package:com.mall.config
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 00:05
 * @Version: v1.0
 *
 */

@Component
@Data
@ConfigurationProperties(prefix = "mall")
public class MallProperties {
    private String name;
    private String version;
    private Order order = new Order();

    @Data
    public static class Order{
        private Integer maxQuantity;
    }


}
