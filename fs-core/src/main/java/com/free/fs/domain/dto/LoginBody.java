package com.free.fs.domain.dto;

//import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "登录实体")
public class LoginBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //    @NotBlank(message = "账号不能为空")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    //    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    //    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    //    @NotBlank(message = "验证码UUID不能为空")
    @Schema(description = "验证码UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;
}