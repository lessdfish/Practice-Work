package com.mall.config;

import com.mall.common.interceptor.RequestTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName:WebMvcConfig
 * Package:com.mall.config
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/28 - 00:15
 * @Version: v1.0
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final RequestTimeInterceptor requestTimeInterceptor;

    public WebMvcConfig(RequestTimeInterceptor requestTimeInterceptor){
        this.requestTimeInterceptor = requestTimeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimeInterceptor).addPathPatterns("/api/**");
    }
}
