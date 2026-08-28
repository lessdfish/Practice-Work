package com.mall.common.exception;

import com.mall.common.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName:GlobalExceptionHandler
 * Package:com.mall.common.exception
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/28 - 23:28
 * @Version: v1.0
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e){
        ErrorCode errorCode = e.getErrorCode();

        Result<Void> result = Result.fail(errorCode.getCode(),e.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationException(MethodArgumentNotValidException e){
        String message = "Request Param error";

        if (e.getBindingResult().getFieldError()!=null) {
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        }

        Result<Void> result = Result.fail(ErrorCode.PARAM_ERROR.getCode(), message);

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e){
        e.printStackTrace();
        Result<Void> result = Result.fail(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(result);
    }
}
