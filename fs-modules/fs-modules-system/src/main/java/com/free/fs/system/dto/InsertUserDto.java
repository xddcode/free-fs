package com.free.fs.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 注册用户dto
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 10:19
 */
@Data
public class InsertUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
