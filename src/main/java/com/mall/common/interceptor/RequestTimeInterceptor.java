package com.mall.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ClassName:RequestTimeInterceptor
 * Package:com.mall.common.interceptor
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/28 - 00:07
 * @Version: v1.0
 *
 */
@Component
public class RequestTimeInterceptor implements HandlerInterceptor {
    private static final String START_TIME = "mall_x_request_start_time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();

        request.setAttribute(START_TIME,startTime);
        System.out.println("MVC request Beginning");
        System.out.println("Method = " + request.getMethod());
        System.out.println("URI = "+ request.getRequestURI());
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        Object start = request.getAttribute(START_TIME);
        if (start instanceof Long startTime) {
            long cost = System.currentTimeMillis() - startTime;
            System.out.println("Status: "+ response.getStatus());
            System.out.println("Cost = "+ cost +"ms");
        }
        if (ex!=null) {
            System.out.println("Exception = " +ex.getMessage());
        }

        System.out.println("MVC request Over! ");

    }
}
