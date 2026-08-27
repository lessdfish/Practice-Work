package com.mall.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.rmi.ServerError;
import java.util.Arrays;

/**
 * ClassName:OperationLogAspect
 * Package:com.mall.common.aspect
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 23:25
 * @Version: v1.0
 *
 */
@Aspect
@Component
public class OperationLogAspect {

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint,
                         OperationLog operationLog) throws Throwable{
        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        String methodName = signature.getMethod().getName();

        Object[] args = joinPoint.getArgs();

        System.out.println("========== Operation Logging ===========");

        System.out.println("Operation Name: " + operationLog.value());
        System.out.println("Target Class: " + className);
        System.out.println("Method Name: " + methodName);
        System.out.println("Args: "+ Arrays.toString(args));
        System.out.println("========================================");

        try {
            Object result = joinPoint.proceed();
            long cost = System.currentTimeMillis() - startTime;

            System.out.println("======Operation Success!========");
            System.out.println("Operation Name: " + operationLog.value());
            System.out.println("Cost Time: "+ cost + "ms");
            System.out.println("================================");
            return result;
        }catch (Throwable e){
            long cost = System.currentTimeMillis() - startTime;

            System.out.println("======Operation File ==========");
            System.err.println("Operation Name: "+operationLog.value());
            System.err.println("Cost Time: "+ cost);
            System.err.println("Error Type : " +e.getClass().getName());
            System.err.println("Error Message: "+ e.getMessage());
            System.err.println("==============================");
            throw e;
        }
    }
}
