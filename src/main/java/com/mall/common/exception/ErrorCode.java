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
    SYSTEM_ERROR(50000,"System Error",HttpStatus.INTERNAL_SERVER_ERROR);

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
