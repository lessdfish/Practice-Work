package com.mall.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ClassName:RegisterRequest
 * Package:com.mall.user.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:39
 * @Version: v1.0
 *
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "Username Not be null")
    @Size(min = 4,max = 32,message = "Length between 4 and 32")
    private String username;

    @NotBlank(message = "Password not be null")
    @Size(min = 6,max = 16,message = "Length between 6 and 16")
    private String password;


}
