package com.mall.common.result;

import lombok.Data;

/**
 * ClassName:Result
 * Package:com.mall.common.result
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:04
 * @Version: v1.0
 *
 */
@Data
public class Result<T>{
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> Result<T> success(T data){
        return new Result<>(0,"success",data);
    }

    public static Result<Void> success(){
        return new Result<>(0,"success",null);
    }

    public static <T> Result<T> fail(int code, String message){
        return new Result<>(code,message,null);
    }

}
