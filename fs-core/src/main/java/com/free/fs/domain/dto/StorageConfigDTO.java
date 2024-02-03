package com.free.fs.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "存储平台配置保存实体")
public class StorageConfigDTO {

    @Schema(description = "配置ID，存在则为更新", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    @Schema(description = "配置数据json字符串", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "配置信息不能为空")
    private String configData;

    @Schema(description = "存储平台标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "存储平台标识不能为空")
    private String platformIdentifier;

//    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Long userId;
}
