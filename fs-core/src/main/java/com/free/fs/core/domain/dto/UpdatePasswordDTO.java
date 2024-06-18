package com.free.fs.core.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改密码DTO
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/17 17:31
 */
@Data
public class UpdatePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "原密码不能为空")
    private String password;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
