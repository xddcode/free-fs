package com.free.fs.core.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件重命名DTO
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/11 11:24
 */
@Data
public class UpdateFileNameDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "文件名不能为空")
    private String name;

    @NotBlank(message = "文件重命名不能为空")
    private String rename;

}
