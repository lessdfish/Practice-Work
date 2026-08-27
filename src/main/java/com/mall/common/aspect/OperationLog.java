package com.mall.common.aspect;

import java.lang.annotation.*;

/**
 * ClassName:OperationLog
 * Package:com.mall.common.aspect
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 23:25
 * @Version: v1.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String value();
}
