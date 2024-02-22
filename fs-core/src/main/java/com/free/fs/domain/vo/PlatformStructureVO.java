package com.free.fs.domain.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Yann
 */
@Data
public class PlatformStructureVO {

    /**
     * 标识
     */
    private String identifier;
    /**
     * 名称
     */
    private String label;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 描述
     */
    private String description;
    /**
     * 校验结构
     */
    private StructureValid validation;

    @Getter
    @Setter
    public static class StructureValid {
        /**
         * 是否必填
         */
        private Boolean required;
    }
}
