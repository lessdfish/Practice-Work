package com.mall.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName:User
 * Package:com.mall.user.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:39
 * @Version: v1.0
 *
 */
@TableName("mall_user")
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
