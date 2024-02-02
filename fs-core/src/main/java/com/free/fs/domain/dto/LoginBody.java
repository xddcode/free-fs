package com.free.fs.domain.dto;

//import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录表单实体
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 15:59
 */
@Data
public class LoginBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

//    @NotBlank(message = "账号不能为空")
    private String username;

//    @NotBlank(message = "密码不能为空")
    private String password;

//    @NotBlank(message = "验证码不能为空")
    private String code;

//    @NotBlank(message = "验证码UUID不能为空")
    private String uuid;
}
