package com.mall.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * ClassName:ErrorCode
 * Package:com.mall.common.exception
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/28 - 23:15
 * @Version: v1.0
 *
 */
public enum ErrorCode{
    SUCCESS(0,"success", HttpStatus.OK),
    PARAM_ERROR(40000,"Param Error",HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(40401,"Product Not Found",HttpStatus.NOT_FOUND),
    PRODUCT_OFF_SHELF(40901,"Product Off Shelf",HttpStatus.CONFLICT),
    STOCK_NOT_ENOUGH(40902,"Stock Not Enough",HttpStatus.CONFLICT),
    ORDER_CREATE_FAILED(50001,"Order Create Failed",HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_ERROR(50000,"System Error",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_EXISTS(40910,"USER_ALREADY_EXISTS",HttpStatus.CONFLICT),
    USER_NOT_FOUND(40410,"USER_NOT_FOUND",HttpStatus.NOT_FOUND),
    USER_DISABLED(40310,"USER_DISABLED",HttpStatus.FORBIDDEN),
    LOGIN_FAILED(40101,"Username or password error",HttpStatus.UNAUTHORIZED),
    ORDER_NOT_FOUND(40420,"Order Not Found" ,HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(40430,"CART_NOT_FOUND" ,HttpStatus.NOT_FOUND),

    INVENTORY_NOT_FOUND(40440,"INVENTORY_NOT_FOUND" , HttpStatus.NOT_FOUND),
    INVENTORY_RESERVATION_NOT_FOUND(40441,"INVENTORY_RESERVATION_NOT_FOUND",HttpStatus.NOT_FOUND),
    INVENTORY_RESERVATION_CONFLICT(40940,"INVENTORY_RESERVATION_CONFLICT",HttpStatus.CONFLICT),

    SECKILL_RATE_LIMITED(42901,"请求过于频繁，请稍后重试",HttpStatus.TOO_MANY_REQUESTS),
    SECKILL_ACTIVITY_NOT_FOUND(40450,"SECKILL_ACTIVITY_NOT_FOUND",HttpStatus.NOT_FOUND),
    SECKILL_ACTIVITY_PREHEATED(50350,"SECKILL_ACTIVITY_Not_PREHEATED",HttpStatus.SERVICE_UNAVAILABLE),
    SECKILL_ACTIVITY_NOT_START(40950,"SECKILL_ACTIVITY_NOT_START",HttpStatus.CONFLICT),
    SECKILL__ENDED(40951,"SECKILL__ENDED",HttpStatus.CONFLICT),
    SECKILL_SOLD_OUT(40952,"SECKILL_SOLD_OUT",HttpStatus.CONFLICT),
    SECKILL_DUPLICATE_ORDER(40953,"SECKILL_DUPLICATE_ORDER",HttpStatus.CONFLICT),
    SECKILL_DISABLED(40954,"SECKILL_DISABLED",HttpStatus.CONFLICT),
    SECKILL_ACTIVITY_NOT_PREHEATED(40956,"SECKILL_ACTIVITY_NOT_PREHEATED" ,HttpStatus.CONFLICT ),
    MQ_PUBLISH_FAILED(50360,"MQ_PUBLISH_FAILED",HttpStatus.SERVICE_UNAVAILABLE),
    SECKILL_RESULT_NOT_FOUND(40451,"Seckill request Not Found" ,HttpStatus.NOT_FOUND ),
    SECKILL_RECONCILE_NOT_ALLOWED(40955,"SECKILL_RECONCILE_NOT_ALLOWED" ,HttpStatus.CONFLICT),

    ORDER_STATUS_ILLEGAL(40920,"ORDER_STATUS_ILLEGAL",HttpStatus.CONFLICT),
    ORDER_STATUS_CONFLICT(40921,"ORDER_STATUS_CONFLICT",HttpStatus.CONFLICT),

    PAYMENT_NOT_FOUND(40460,"PAYMENT_NOT_FOUND",HttpStatus.NOT_FOUND),
    ORDER_NOT_PAYABLE(40960,"ORDER_NOT_PAYABLE",HttpStatus.CONFLICT),
    PAYMENT_AMOUNT_MISMATCH(40961,"PAYMENT_AMOUNT_MISMATCH",HttpStatus.CONFLICT),
    PAYMENT_CALLBACK_CONFLICT(40962,"PAYMENT_CALLBACK_CONFLICT",HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
