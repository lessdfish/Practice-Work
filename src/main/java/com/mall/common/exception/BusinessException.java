package com.mall.common.exception;

/**
 * ClassName:BusinessException
 * Package:com.mall.common.exception
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/28 - 23:15
 * @Version: v1.0
 *
 */
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
